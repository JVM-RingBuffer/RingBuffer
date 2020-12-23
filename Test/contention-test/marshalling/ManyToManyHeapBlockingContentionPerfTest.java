package test.marshalling;

import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyToManyHeapBlockingContentionPerfTest extends ManyToManyHeapBlockingContentionTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyHeapBlockingContentionPerfTest().runBenchmark();
    }

    HeapRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
