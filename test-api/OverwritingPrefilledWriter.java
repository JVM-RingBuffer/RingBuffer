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
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static OverwritingPrefilledWriter startAsync(int numIterations, OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        OverwritingPrefilledWriter writer = new OverwritingPrefilledWriter(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private OverwritingPrefilledWriter(int numIterations, OverwritingPrefilledRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    String getProfilerName() {
        return "OverwritingPrefilledWriter";
    }

    @Override
    void loop() {
        OverwritingPrefilledRingBuffer<Event> ringBuffer = getOverwritingPrefilledRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            ringBuffer.next(key).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}
