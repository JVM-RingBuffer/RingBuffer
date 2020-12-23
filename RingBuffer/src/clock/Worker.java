package org.ringbuffer.clock;

import eu.menzani.atomic.AtomicLong;
import eu.menzani.system.ThreadManipulation;
import eu.menzani.system.Unsafe;
import org.ringbuffer.object.PrefilledRingBuffer;

class Worker extends Thread {
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
        threadManipulation.applyToCurrentThread();

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
            AtomicLong.setOpaque(BusyWaitClock.TIME_base, BusyWaitClock.TIME_offset, time);

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
}
