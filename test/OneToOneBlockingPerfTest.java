package test;

import eu.menzani.ringbuffer.RingBuffer;

public class OneToOneBlockingPerfTest extends OneToOneBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
