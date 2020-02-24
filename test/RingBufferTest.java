package eu.menzani.ringbuffer;

import org.junit.Test;

import static org.junit.Assert.*;

abstract class RingBufferTest {
    static final int SMALL_BUFFER_SIZE = 5;

    static final int NUM_ITERATIONS = 1_000_000;
    static final int CONCURRENCY = 6;
    static final int TOTAL_ELEMENTS = CONCURRENCY * NUM_ITERATIONS;

    private final Class<? extends RingBuffer<?>> clazz;
    private final RingBuffer<Event> ringBuffer;

    <T extends RingBuffer<?>> RingBufferTest(Class<T> clazz, RingBufferBuilder<Event> builder) {
        this.clazz = clazz;
        ringBuffer = builder.build();
    }

    @Test
    public void testClass() {
        assertEquals(clazz, ringBuffer.getClass());
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        assertEquals(run(), run());
    }

    abstract int run() throws InterruptedException;

    private abstract static class Thread extends java.lang.Thread {
        private final int numIterations;

        private Thread(int numIterations) {
            this.numIterations = numIterations;
            start();
        }

        @Override
        public void run() {
            for (int i = 0; i < numIterations; i++) {
                tick(i);
            }
        }

        abstract void tick(int i);
    }

    class Reader extends Thread {
        private int sum;

        Reader(int numIterations) {
            super(numIterations);
        }

        int getSum() {
            return sum;
        }

        @Override
        void tick(int i) {
            sum += ringBuffer.take().getData();
        }
    }

    class Writer extends Thread {
        Writer(int numIterations) {
            super(numIterations);
        }

        @Override
        void tick(int i) {
            Event event = new Event();
            event.setData(i);
            ringBuffer.put(event);
        }
    }

    class PrefilledWriter extends Thread {
        PrefilledWriter(int numIterations) {
            super(numIterations);
        }

        @Override
        void tick(int i) {
            Event event = ringBuffer.next();
            event.setData(i);
            ringBuffer.put();
        }
    }

    class PrefilledSynchronizedWriter extends PrefilledWriter {
        PrefilledSynchronizedWriter(int numIterations) {
            super(numIterations);
        }

        @Override
        void tick(int i) {
            synchronized (ringBuffer) {
                super.tick(i);
            }
        }
    }
}
