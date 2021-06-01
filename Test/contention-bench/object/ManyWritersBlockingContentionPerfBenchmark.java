package bench.object;

import org.ringbuffer.object.RingBuffer;

public class ManyWritersBlockingContentionPerfBenchmark extends ManyWritersBlockingContentionBenchmark {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersBlockingContentionPerfBenchmark().runBenchmark();
    }

    RingBuffer<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
