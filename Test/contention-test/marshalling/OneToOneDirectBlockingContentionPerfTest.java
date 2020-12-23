package test.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class OneToOneDirectBlockingContentionPerfTest extends OneToOneDirectBlockingContentionTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneDirectBlockingContentionPerfTest().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
