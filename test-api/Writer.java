package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(RingBuffer<Event> ringBuffer) {
        startGroupAsync(ringBuffer).reportPerformance();
    }

    static Writer startAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.start();
        return writer;
    }

    static void runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        startAsync(numIterations, ringBuffer).reportPerformance();
    }

    static void runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.run();
        writer.reportPerformance();
    }

    private Writer(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        EmptyRingBuffer<Event> ringBuffer = getEmptyRingBuffer();
        for (; numIterations > 0; numIterations--) {
            ringBuffer.put(new Event(numIterations));
        }
    }
}
