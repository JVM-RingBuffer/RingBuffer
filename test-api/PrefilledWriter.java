package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;

class PrefilledWriter extends TestThread {
    static PrefilledWriter startAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        PrefilledWriter writer = new PrefilledWriter(numIterations, ringBuffer);
        writer.start();
        return writer;
    }

    static void runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).reportPerformance();
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
        PrefilledRingBuffer<Event> ringBuffer = getPrefilledRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            Event event = ringBuffer.next();
            event.setData(i);
            ringBuffer.put();
        }
    }
}
