package bench.marshalling;

import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyToManyDirectBlockingContentionPerfBenchmark extends ManyToManyDirectBlockingContentionBenchmark {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyDirectBlockingContentionPerfBenchmark().runBenchmark();
    }

    DirectRingBuffer getRingBuffer() {
        return RING_BUFFER;
    }
}
