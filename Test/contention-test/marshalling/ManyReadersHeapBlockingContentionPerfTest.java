package test.marshalling;

import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyReadersHeapBlockingContentionPerfTest extends ManyReadersHeapBlockingContentionTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersHeapBlockingContentionPerfTest().runBenchmark();
    }

    HeapRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
