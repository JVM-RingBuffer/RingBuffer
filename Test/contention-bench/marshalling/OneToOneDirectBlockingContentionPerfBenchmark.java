package bench.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class OneToOneDirectBlockingContentionPerfBenchmark extends OneToOneDirectBlockingContentionBenchmark {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneDirectBlockingContentionPerfBenchmark().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
