package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.java.MutableLong;

class SynchronizedDisposingBatchReader extends BatchReader {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> new SynchronizedDisposingBatchReader(numIterations, ringBuffer));
    }

    private SynchronizedDisposingBatchReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    private static final Array<Event> readBuffer = newReadBuffer();

    @Override
    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = readBuffer;
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                ringBuffer.take(buffer);
                for (Event event : buffer) {
                    sum.add(event.getData());
                }
                ringBuffer.dispose();
            }
        }
    }
}
