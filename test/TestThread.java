package eu.menzani.ringbuffer;

abstract class TestThread extends Thread {
    private final int numIterations;
    final RingBuffer<Event> ringBuffer;

    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        this.numIterations = numIterations;
        this.ringBuffer = ringBuffer;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < numIterations; i++) {
            tick(i);
        }
    }

    abstract void tick(int i);
}
