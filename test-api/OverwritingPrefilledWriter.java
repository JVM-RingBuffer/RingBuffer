package test;

import eu.menzani.ringbuffer.OverwritingPrefilledRingBuffer;

class OverwritingPrefilledWriter extends TestThread {
    static TestThreadGroup startGroupAsync(OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(new Factory() {
            @Override
            public TestThread newInstance(int numIterations) {
                return new OverwritingPrefilledWriter(numIterations, ringBuffer);
            }
        });
        group.start();
        return group;
    }

    static void runGroupAsync(OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).reportPerformance();
    }

    static OverwritingPrefilledWriter startAsync(int numIterations, OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        OverwritingPrefilledWriter writer = new OverwritingPrefilledWriter(numIterations, ringBuffer);
        writer.start();
        return writer;
    }

    static void runAsync(int numIterations, OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).reportPerformance();
    }

    private OverwritingPrefilledWriter(int numIterations, OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        OverwritingPrefilledRingBuffer<Event> ringBuffer = getOverwritingPrefilledRingBuffer();
        for (; numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            ringBuffer.next(key).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}
