package bench.object;

import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledOneToOneBlockingContentionPerfBenchmark extends PrefilledOneToOneBlockingContentionBenchmark {
    public static final PrefilledRingBuffer2<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingContentionPerfBenchmark().runBenchmark();
    }

    PrefilledRingBuffer2<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
