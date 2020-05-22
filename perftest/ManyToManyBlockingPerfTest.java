package test;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;

public class ManyToManyBlockingPerfTest extends ManyToManyBlockingTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyBlockingPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
