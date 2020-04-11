package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledWriter extends TestThread {
    static void runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledWriter writer = new PrefilledWriter(numIterations, ringBuffer);
        writer.start();
        writer.reportPerformance();
    }

    static void runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledWriter writer = new PrefilledWriter(numIterations, ringBuffer);
        writer.run();
        writer.reportPerformance();
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
