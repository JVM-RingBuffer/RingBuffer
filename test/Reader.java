package eu.menzani.ringbuffer;

class Reader extends TestThread {
    private int sum;

    Reader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    int getSum() {
        return sum;
    }

    @Override
    void tick(int i) {
        sum += ringBuffer.take().getData();
    }
}
