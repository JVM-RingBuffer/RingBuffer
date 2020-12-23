package test.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyToManyDirectBlockingContentionPerfTest extends ManyToManyDirectBlockingContentionTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyDirectBlockingContentionPerfTest().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
