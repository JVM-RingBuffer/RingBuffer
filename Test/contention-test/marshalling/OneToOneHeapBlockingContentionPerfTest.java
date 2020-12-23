package test.marshalling;

import org.ringbuffer.marshalling.HeapRingBuffer;

public class OneToOneHeapBlockingContentionPerfTest extends OneToOneHeapBlockingContentionTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneHeapBlockingContentionPerfTest().runBenchmark();
    }

    HeapRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
