package test.object;

import org.ringbuffer.object.RingBuffer;

public class ManyToManyBlockingContentionPerfTest extends ManyToManyBlockingContentionTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyBlockingContentionPerfTest().runBenchmark();
    }

    RingBuffer<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
