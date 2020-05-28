/*
 * Copyright 2020 Francesco Menzani
 *
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
import org.ringbuffer.concurrent.AtomicBoolean;

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
import java.util.function.Consumer;

public class GarbageCollectorProfiler {
    private static final AtomicBoolean jvmListenerRegistered = new AtomicBoolean();
    private static volatile JVMListener jvmListener;

    public static void logGCs() {
        addListener(new Logger());
    }

    public static void addListener(Listener listener) {
        if (jvmListenerRegistered.compareAndSetVolatile(false, true)) {
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
            GarbageCollectorProfiler.jvmListener = jvmListener;
        }
        jvmListener.addUserListener(listener);
    }

    public static long sumValuesToMB(Map<String, MemoryUsage> memoryUsage) {
        return sumValues(memoryUsage) / (1024L * 1024L);
    }

    private static final ValuesSumAction valuesSumAction = new ValuesSumAction();

    public static long sumValues(Map<String, MemoryUsage> memoryUsage) {
        valuesSumAction.resetTotal();
        memoryUsage.values().forEach(valuesSumAction); // Should create no garbage
        return valuesSumAction.getTotal();
    }

    public interface Listener {
        void onEvent(GarbageCollectionNotificationInfo notification, GcInfo info);
    }

    private static class Logger implements Listener {
        Logger() {}

        @Override
        public void onEvent(GarbageCollectionNotificationInfo notification, GcInfo info) {
            synchronized (System.out) {
                System.out.print('[');
                System.out.print(notification.getGcName());
                System.out.print("] ");
                System.out.print(notification.getGcAction());
                System.out.print(":  ");
                System.out.print(sumValuesToMB(info.getMemoryUsageBeforeGc()));
                System.out.print(" MB -> ");
                System.out.print(sumValuesToMB(info.getMemoryUsageAfterGc()));
                System.out.print(" MB  ");
                System.out.print(info.getDuration());
                System.out.print("ms");
                System.out.println();
            }
        }
    }

    private static class JVMListener implements NotificationListener {
        private final List<Listener> userListeners = new CopyOnWriteArrayList<>();

        JVMListener() {}

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

    private static class ValuesSumAction implements Consumer<MemoryUsage> {
        private long total;

        ValuesSumAction() {}

        void resetTotal() {
            total = 0L;
        }

        long getTotal() {
            return total;
        }

        @Override
        public void accept(MemoryUsage memoryUsage) {
            total += memoryUsage.getUsed();
        }
    }
}
