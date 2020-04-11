package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class SynchronizedAdvancingBatchReader extends BatchReader {
    static TestThreadGroup runGroupAsync(int readBufferSize, RingBuffer<Event> ringBuffer) {
        Event[] readBuffer = new Event[readBufferSize];
        return new TestThreadGroup(numIterations -> runAsync(numIterations, readBuffer, ringBuffer));
    }

    private static SynchronizedAdvancingBatchReader runAsync(int numIterations, Event[] readBuffer, RingBuffer<Event> ringBuffer) {
        SynchronizedAdvancingBatchReader thread = new SynchronizedAdvancingBatchReader(numIterations, readBuffer, ringBuffer);
        thread.start();
        return thread;
    }

    private SynchronizedAdvancingBatchReader(int numIterations, Event[] readBuffer, RingBuffer<Event> ringBuffer) {
        super(numIterations, readBuffer, ringBuffer);
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Event[] buffer = getReadBuffer();
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                ringBuffer.fill(buffer);
                for (Event event : buffer) {
                    sum += event.getData();
                }
                ringBuffer.advanceBatch();
            }
        }
        return sum;
    }
}
