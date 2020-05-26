package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;
import eu.menzani.ringbuffer.object.RingBuffer;
import test.TestThreadGroup;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(RingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static Writer startAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private Writer(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        EmptyRingBuffer<Event> ringBuffer = getEmptyRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            ringBuffer.put(new Event(numIterations));
        }
    }
}
