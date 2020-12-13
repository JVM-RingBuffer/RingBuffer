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

package org.ringbuffer.util;

import org.ringbuffer.concurrent.AtomicLong;
import org.ringbuffer.lang.Lang;
import org.ringbuffer.lang.StaticFieldOffset;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.system.ThreadManipulation;
import org.ringbuffer.system.Unsafe;

public class BusyWaitClock {
    private static final Object TIME_base;
    private static final long TIME_offset;

    private static final ObjectToggle<Thread> worker = new SimpleObjectToggle<>();

    static {
        StaticFieldOffset TIME = Lang.staticFieldOffset(BusyWaitClock.class, "time");
        TIME_base = TIME.getBase();
        TIME_offset = TIME.getOffset();
    }

    private static long time;

    public static void start() {
        start(ThreadManipulation.doNothing());
    }

    public static void start(ThreadManipulation threadManipulation) {
        synchronized (worker) {
            worker.ensureNotSet("Clock is already running.");
            Thread worker = new Worker(threadManipulation);
            worker.setDaemon(true);
            worker.start();
            BusyWaitClock.worker.set(worker);
        }
    }

    public static void stop() throws InterruptedException {
        synchronized (worker) {
            Thread worker = BusyWaitClock.worker.ensureSet("Clock is not running.");
            Worker.terminate();
            worker.join();
        }
    }

    public static long currentTime() {
        return AtomicLong.getOpaque(TIME_base, TIME_offset);
    }

    public static void sleepCurrentThread(int nanoseconds) {
        Worker.sleep(Thread.currentThread(), currentTime() + nanoseconds);
        Unsafe.park(0L);
    }

    private static class Worker extends Thread {
        private static final int maxParkedThreads = 10;

        private static final PrefilledRingBuffer<ParkedThread> ringBuffer =
                PrefilledRingBuffer.<ParkedThread>withCapacity(maxParkedThreads + 1)
                        .fillWith(ParkedThread::new)
                        .oneReader()
                        .oneWriter()
                        .build();

        static void sleep(Thread thread, long endTime) {
            int key = ringBuffer.nextKey();
            ParkedThread parkedThread = ringBuffer.next(key);
            parkedThread.thread = thread;
            parkedThread.endTime = endTime;
            ringBuffer.put(key);
        }

        static void terminate() {
            int key = ringBuffer.nextKey();
            ParkedThread parkedThread = ringBuffer.next(key);
            parkedThread.shouldTerminate = true;
            ringBuffer.put(key);
        }

        private final ThreadManipulation threadManipulation;

        Worker(ThreadManipulation threadManipulation) {
            super("BusyWaitClock");
            this.threadManipulation = threadManipulation;
        }

        @Override
        public void run() {
            threadManipulation.run();

            ParkedThread[] parkedThreads = new ParkedThread[maxParkedThreads];
            int parkedThreadsCount = 0;

            while (true) {
                if (!ringBuffer.isEmpty()) {
                    ParkedThread parkedThread = ringBuffer.take();
                    parkedThreads[parkedThreadsCount++] = parkedThread;
                    if (parkedThread.shouldTerminate) {
                        unparkAll(parkedThreads, parkedThreadsCount);
                        break;
                    }
                }

                long time = System.nanoTime();
                AtomicLong.setOpaque(TIME_base, TIME_offset, time);

                for (int i = 0, end = parkedThreadsCount; i < end; i++) {
                    ParkedThread parkedThread = parkedThreads[i];
                    if (parkedThread.endTime <= time) {
                        Unsafe.unpark(parkedThread.thread);
                        parkedThreadsCount--;
                    }
                }
            }
        }

        private static void unparkAll(ParkedThread[] parkedThreads, int count) {
            parkedThreads[count - 1].shouldTerminate = false;
            for (int i = 0; i < count; i++) {
                Unsafe.unpark(parkedThreads[i].thread);
            }
        }

        private static class ParkedThread {
            Thread thread;
            long endTime;

            boolean shouldTerminate;
        }
    }
}
