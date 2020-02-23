package eu.menzani.ringbuffer;

class Test {
    static RingBuffer<Event> ringBuffer;

    static final int NUM_ITERATIONS = 1_000_000;
    static final int CONCURRENCY = 6;
    static final int TOTAL_ELEMENTS = CONCURRENCY * NUM_ITERATIONS;
}
