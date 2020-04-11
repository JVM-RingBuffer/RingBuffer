package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class Writer extends TestThread {
    static void runGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start();
        group.reportPerformance();
    }

    static void runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.start();
        writer.reportPerformance();
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
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.put(new Event(i));
        }
    }
}
