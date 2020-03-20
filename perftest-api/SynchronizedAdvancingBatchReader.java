package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.java.MutableLong;

class SynchronizedAdvancingBatchReader extends BatchReader {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        Array<Event> readBuffer = newReadBuffer();
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer, readBuffer));
    }

    private static SynchronizedAdvancingBatchReader runAsync(int numIterations, RingBuffer<Event> ringBuffer, Array<Event> readBuffer) {
        SynchronizedAdvancingBatchReader thread = new SynchronizedAdvancingBatchReader(numIterations, ringBuffer, readBuffer);
        thread.start();
        return thread;
    }

    private final Array<Event> readBuffer;

    private SynchronizedAdvancingBatchReader(int numIterations, RingBuffer<Event> ringBuffer, Array<Event> readBuffer) {
        super(numIterations, ringBuffer);
        this.readBuffer = readBuffer;
    }

    @Override
    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = readBuffer;
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                ringBuffer.fill(buffer);
                for (Event event : buffer) {
                    sum.add(event.getData());
                }
                ringBuffer.advance();
            }
        }
    }
}
