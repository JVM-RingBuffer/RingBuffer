package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;

class PrefilledWriter extends TestThread {
    static TestThreadGroup startGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(RingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).reportPerformance();
    }

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
            ringBuffer.next().setData(i);
            ringBuffer.put();
        }
    }
}
