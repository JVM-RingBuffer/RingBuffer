package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.MutableLong;

class Reader extends TestThread {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer));
    }

    static Reader runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader thread = new Reader(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    static Reader runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader thread = new Reader(numIterations, ringBuffer);
        thread.run();
        return thread;
    }

    private final MutableLong sum = new MutableLong();

    Reader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    long getSum() {
        return sum.get();
    }

    @Override
    void loop() {
        collect(sum);
    }

    void collect(MutableLong sum) {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            sum.add(ringBuffer.take().getData());
        }
    }

    @Override
    void waitForCompletion() {
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}
