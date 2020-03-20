package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.java.MutableInt;
import eu.menzani.ringbuffer.java.MutableLong;

class BatchReader extends Reader {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer));
    }

    static BatchReader runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        BatchReader thread = new BatchReader(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    static BatchReader runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        BatchReader thread = new BatchReader(numIterations, ringBuffer);
        thread.run();
        return thread;
    }

    BatchReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(MutableInt.ceilDiv(numIterations, RingBufferTest.READ_BUFFER_SIZE), ringBuffer);
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
        }
    }

    static Array<Event> newReadBuffer() {
        return new Array<>(RingBufferTest.READ_BUFFER_SIZE);
    }
}
