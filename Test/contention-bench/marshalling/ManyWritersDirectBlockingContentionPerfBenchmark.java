package bench.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyWritersDirectBlockingContentionPerfBenchmark extends ManyWritersDirectBlockingContentionBenchmark {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersDirectBlockingContentionPerfBenchmark().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
