package test;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

class PrefilledWriter extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledRingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static PrefilledWriter startAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        PrefilledWriter writer = new PrefilledWriter(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private PrefilledWriter(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "PrefilledWriter";
    }

    @Override
    protected void loop() {
        PrefilledRingBuffer<Event> ringBuffer = getPrefilledRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            int putKey = ringBuffer.nextPutKey(key);
            ringBuffer.next(key, putKey).setData(numIterations);
            ringBuffer.put(putKey);
        }
    }
}
