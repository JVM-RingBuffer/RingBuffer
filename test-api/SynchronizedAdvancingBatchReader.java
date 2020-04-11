package test;

import eu.menzani.ringbuffer.RingBuffer;

class SynchronizedAdvancingBatchReader extends BatchReader {
    static long runGroupAsync(int readBufferSize, RingBuffer<Event> ringBuffer) {
        Event[] readBuffer = new Event[readBufferSize];
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedAdvancingBatchReader(numIterations, readBuffer, ringBuffer));
        group.start();
        group.reportPerformance();
        return group.getReaderSum();
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
