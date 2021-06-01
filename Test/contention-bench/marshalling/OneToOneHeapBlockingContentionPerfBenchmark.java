package bench.marshalling;

import org.ringbuffer.marshalling.HeapRingBuffer;

public class OneToOneHeapBlockingContentionPerfBenchmark extends OneToOneHeapBlockingContentionBenchmark {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneHeapBlockingContentionPerfBenchmark().runBenchmark();
    }

    HeapRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
