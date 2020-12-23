package test.object;

import org.ringbuffer.object.RingBuffer;

public class OneToOneBlockingContentionPerfTest extends OneToOneBlockingContentionTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneBlockingContentionPerfTest().runBenchmark();
    }

    RingBuffer<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
