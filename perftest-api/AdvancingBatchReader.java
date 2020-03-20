package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.java.MutableLong;

class AdvancingBatchReader extends BatchReader {
    static AdvancingBatchReader runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        AdvancingBatchReader thread = new AdvancingBatchReader(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    private AdvancingBatchReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = newReadBuffer();
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.fill(buffer);
            for (Event event : buffer) {
                sum.add(event.getData());
            }
            ringBuffer.advance();
        }
    }
}
