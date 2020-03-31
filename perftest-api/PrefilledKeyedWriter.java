package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledKeyedWriter extends TestThread {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer));
    }

    private static PrefilledKeyedWriter runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledKeyedWriter thread = new PrefilledKeyedWriter(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    private PrefilledKeyedWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            int key = ringBuffer.nextKey();
            Event event = ringBuffer.next(key);
            event.setData(i);
            ringBuffer.put(key);
        }
    }
}
