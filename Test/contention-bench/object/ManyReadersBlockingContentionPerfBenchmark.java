package bench.object;

import org.ringbuffer.object.RingBuffer;

public class ManyReadersBlockingContentionPerfBenchmark extends ManyReadersBlockingContentionBenchmark {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersBlockingContentionPerfBenchmark().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
