package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledSynchronizedWriter extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> new PrefilledSynchronizedWriter(numIterations, ringBuffer));
    }

    private PrefilledSynchronizedWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                Event event = ringBuffer.next();
                event.setData(i);
                ringBuffer.put();
            }
        }
    }
}
