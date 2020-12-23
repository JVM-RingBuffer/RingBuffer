package test.marshalling;

import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyWritersHeapBlockingContentionPerfTest extends ManyWritersHeapBlockingContentionTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersHeapBlockingContentionPerfTest().runBenchmark();
    }

    HeapRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
