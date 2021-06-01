package bench.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyReadersDirectBlockingContentionPerfBenchmark extends ManyReadersDirectBlockingContentionBenchmark {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersDirectBlockingContentionPerfBenchmark().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
