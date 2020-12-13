/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.system;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import org.ringbuffer.InternalUnsafe;
import org.ringbuffer.lang.StreamBuffer;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Garbage {
    public static void freeMemory(long address) {
        InternalUnsafe.UNSAFE.freeMemory(address);
    }

    public static void freeMemory(Object object, long... addresses) {
        Cleaner.value.register(object, new FreeMemory(addresses));
    }

    private static class Cleaner {
        static final java.lang.ref.Cleaner value = java.lang.ref.Cleaner.create();
    }

    private static class FreeMemory implements Runnable {
        private final long[] addresses;

        FreeMemory(long[] addresses) {
            this.addresses = addresses;
        }

        @Override
        public void run() {
            for (long address : addresses) {
                freeMemory(address);
            }
        }
    }

    public static void logGCs() {
        addGCListener(new Logger());
    }

    private static class Logger implements Listener {
        private static final StreamBuffer buffer = StreamBuffer.standardOutput(128);

        @Override
        public void onEvent(GarbageCollectionNotificationInfo notification, GcInfo info) {
            buffer.reset();
            buffer.builder.append('[');
            buffer.builder.append(notification.getGcName());
            buffer.builder.append("] ");
            buffer.builder.append(notification.getGcAction());
            buffer.builder.append(":  ");
            buffer.builder.append(sumValuesToMB(info.getMemoryUsageBeforeGc()));
            buffer.builder.append(" MB -> ");
            buffer.builder.append(sumValuesToMB(info.getMemoryUsageAfterGc()));
            buffer.builder.append(" MB  ");
            buffer.builder.append(info.getDuration());
            buffer.builder.append("ms");
            buffer.println();
        }
    }

    public static long sumValuesToMB(Map<String, MemoryUsage> memoryUsage) {
        return sumValues(memoryUsage) / (1024L * 1024L);
    }

    private static final ValuesSumAction valuesSumAction = new ValuesSumAction();

    private static class ValuesSumAction implements Consumer<MemoryUsage> {
        long total;

        @Override
        public void accept(MemoryUsage memoryUsage) {
            total += memoryUsage.getUsed();
        }
    }

    public static long sumValues(Map<String, MemoryUsage> memoryUsage) {
        synchronized (valuesSumAction) {
            valuesSumAction.total = 0L;
            memoryUsage.values().forEach(valuesSumAction); // Should create no garbage
            return valuesSumAction.total;
        }
    }

    private static final AtomicBoolean jvmListenerRegistered = new AtomicBoolean();
    private static volatile JVMListener jvmListener;

    public static void addGCListener(Listener listener) {
        if (jvmListenerRegistered.compareAndSet(false, true)) {
            MBeanServer platformServer = ManagementFactory.getPlatformMBeanServer();
            JVMListener jvmListener = new JVMListener();
            try {
                for (GarbageCollectorMXBean garbageCollectorMX : ManagementFactory.getGarbageCollectorMXBeans()) {
                    platformServer.addNotificationListener(garbageCollectorMX.getObjectName(), jvmListener, null, null);
                }
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
                return;
            }
            Garbage.jvmListener = jvmListener;
        }
        jvmListener.addUserListener(listener);
    }

    public interface Listener {
        void onEvent(GarbageCollectionNotificationInfo notification, GcInfo info);
    }

    private static class JVMListener implements NotificationListener {
        private final List<Listener> userListeners = new CopyOnWriteArrayList<>();

        void addUserListener(Listener userListener) {
            userListeners.add(userListener);
        }

        @Override
        public void handleNotification(Notification notification, Object handback) {
            GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
            GcInfo gcInfo = notificationInfo.getGcInfo();
            for (int i = 0; i < userListeners.size(); i++) {
                userListeners.get(i).onEvent(notificationInfo, gcInfo);
            }
        }
    }
}
