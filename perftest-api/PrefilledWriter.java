package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledWriter extends TestThread {
    static PrefilledWriter runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledWriter thread = new PrefilledWriter(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    static PrefilledWriter runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledWriter thread = new PrefilledWriter(numIterations, ringBuffer);
        thread.run();
        return thread;
    }

    private PrefilledWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            Event event = ringBuffer.next();
            event.setData(i);
            ringBuffer.put();
        }
    }
}
