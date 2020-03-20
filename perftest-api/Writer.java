package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class Writer extends TestThread {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer));
    }

    static Writer runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Writer thread = new Writer(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    static Writer runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        Writer thread = new Writer(numIterations, ringBuffer);
        thread.run();
        return thread;
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
