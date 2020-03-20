package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledSynchronizedWriter extends TestThread {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer));
    }

    private static PrefilledSynchronizedWriter runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledSynchronizedWriter thread = new PrefilledSynchronizedWriter(numIterations, ringBuffer);
        thread.start();
        return thread;
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
