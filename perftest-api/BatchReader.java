package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.java.MutableLong;

class BatchReader extends Reader {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> new BatchReader(numIterations, ringBuffer));
    }

    BatchReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations / RingBufferTest.READ_BUFFER_SIZE, ringBuffer);
    }

    static Array<Event> newReadBuffer() {
        return new Array<>(RingBufferTest.READ_BUFFER_SIZE);
    }

    @Override
    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = newReadBuffer();
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.take(buffer);
            for (Event event : buffer) {
                sum.add(event.getData());
            }
        }
    }
}
