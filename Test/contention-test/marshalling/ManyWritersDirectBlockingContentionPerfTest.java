package test.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyWritersDirectBlockingContentionPerfTest extends ManyWritersDirectBlockingContentionTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersDirectBlockingContentionPerfTest().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
