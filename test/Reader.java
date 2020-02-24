package eu.menzani.ringbuffer;

class Reader extends TestThread {
    private long sum;

    Reader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    long getSum() {
        return sum;
    }

    @Override
    void tick(int i) {
        sum += ringBuffer.take().getData();
    }
}
