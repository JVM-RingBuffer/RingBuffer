package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;

class AdvancingBatchReader extends BatchReader {
    static AdvancingBatchReader runAsync(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        AdvancingBatchReader thread = new AdvancingBatchReader(numIterations, readBufferSize, ringBuffer);
        thread.start();
        return thread;
    }

    private AdvancingBatchReader(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        super(numIterations, readBufferSize, ringBuffer);
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = getReadBuffer();
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.fill(buffer);
            for (Event event : buffer) {
                sum += event.getData();
            }
            ringBuffer.advanceBatch();
        }
        return sum;
    }
}
