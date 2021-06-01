package bench.marshalling;

import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyWritersHeapBlockingContentionPerfBenchmark extends ManyWritersHeapBlockingContentionBenchmark {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersHeapBlockingContentionPerfBenchmark().runBenchmark();
    }

    HeapRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
