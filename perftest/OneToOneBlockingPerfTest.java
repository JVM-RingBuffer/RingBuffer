package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class OneToOneBlockingPerfTest extends OneToOneBlockingTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneBlockingPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
