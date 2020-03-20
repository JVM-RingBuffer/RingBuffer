package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.MutableLong;

class SynchronizedReader extends Reader {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runThreadAsync(numIterations, ringBuffer));
    }

    private static SynchronizedReader runThreadAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        SynchronizedReader thread = new SynchronizedReader(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    private SynchronizedReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                sum.add(ringBuffer.take().getData());
            }
        }
    }
}
