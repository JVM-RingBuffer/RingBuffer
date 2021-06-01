package bench.object;

import org.ringbuffer.object.RingBuffer;

public class OneToOneBlockingContentionPerfBenchmark extends OneToOneBlockingContentionBenchmark {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneBlockingContentionPerfBenchmark().runBenchmark();
    }

    RingBuffer<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
