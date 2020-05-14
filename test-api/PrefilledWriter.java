package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

class PrefilledWriter extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledRingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).reportPerformance();
    }

    static PrefilledWriter startAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        PrefilledWriter writer = new PrefilledWriter(numIterations, ringBuffer);
        writer.start();
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).reportPerformance();
    }

    private PrefilledWriter(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        PrefilledRingBuffer<Event> ringBuffer = getPrefilledRingBuffer();
        for (; numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            int putKey = ringBuffer.nextPutKey(key);
            ringBuffer.next(key, putKey).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}
