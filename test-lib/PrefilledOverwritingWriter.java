package test;

import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBuffer;

class PrefilledOverwritingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(new Factory() {
            @Override
            public TestThread newInstance(int numIterations) {
                return new PrefilledOverwritingWriter(numIterations, ringBuffer);
            }
        });
        group.start();
        return group;
    }

    static void runGroupAsync(PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static PrefilledOverwritingWriter startAsync(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        PrefilledOverwritingWriter writer = new PrefilledOverwritingWriter(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private PrefilledOverwritingWriter(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    String getProfilerName() {
        return "PrefilledOverwritingWriter";
    }

    @Override
    void loop() {
        PrefilledOverwritingRingBuffer<Event> ringBuffer = getPrefilledOverwritingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            ringBuffer.next(key).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}
