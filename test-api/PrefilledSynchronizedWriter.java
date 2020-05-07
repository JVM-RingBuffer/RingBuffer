package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;

class PrefilledSynchronizedWriter extends TestThread {
    static TestThreadGroup startGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledSynchronizedWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(RingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).reportPerformance();
    }

    private PrefilledSynchronizedWriter(int numIterations, RingBuffer<Event> ringBuffer) {
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
