package eu.menzani.ringbuffer.system;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GCProfiler implements NotificationListener {
    private static final AtomicBoolean started = new AtomicBoolean();

    public static void start() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("The profiler has already been started.");
        }
        try {
            MBeanServer platformServer = ManagementFactory.getPlatformMBeanServer();
            for (GarbageCollectorMXBean garbageCollectorMX : ManagementFactory.getGarbageCollectorMXBeans()) {
                platformServer.addNotificationListener(garbageCollectorMX.getObjectName(), new GCProfiler(), null, null);
            }
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }

    private GCProfiler() {}

    @Override
    public void handleNotification(Notification notification, Object handback) {
        GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
        GcInfo gcInfo = notificationInfo.getGcInfo();
        System.out.println('[' + notificationInfo.getGcName() + "] " + notificationInfo.getGcAction() + ":  " + sumValues(gcInfo.getMemoryUsageBeforeGc()) + " MB -> " + sumValues(gcInfo.getMemoryUsageAfterGc()) + " MB  " + gcInfo.getDuration() + "ms");
    }

    private static long sumValues(Map<String, MemoryUsage> memoryUsage) {
        long total = 0L;
        for (MemoryUsage value : memoryUsage.values()) {
            total += value.getUsed();
        }
        return total / (1024L * 1024L);
    }
}
