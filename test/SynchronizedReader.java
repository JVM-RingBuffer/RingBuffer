package eu.menzani.ringbuffer;

class SynchronizedReader extends Reader {
    SynchronizedReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void tick(int i) {
        synchronized (ringBuffer) {
            super.tick(i);
        }
    }
}
