package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.java.MutableLong;

class SynchronizedAdvancingBatchReader extends BatchReader {
    static TestThreadGroup runGroupAsync(int readBufferSize, RingBuffer<Event> ringBuffer) {
        Array<Event> readBuffer = new Array<>(readBufferSize);
        return new TestThreadGroup(numIterations -> runAsync(numIterations, readBuffer, ringBuffer));
    }

    private static SynchronizedAdvancingBatchReader runAsync(int numIterations, Array<Event> readBuffer, RingBuffer<Event> ringBuffer) {
        SynchronizedAdvancingBatchReader thread = new SynchronizedAdvancingBatchReader(numIterations, readBuffer, ringBuffer);
        thread.start();
        return thread;
    }

    private SynchronizedAdvancingBatchReader(int numIterations, Array<Event> readBuffer, RingBuffer<Event> ringBuffer) {
        super(numIterations, readBuffer, ringBuffer);
    }

    @Override
    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = getReadBuffer();
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
