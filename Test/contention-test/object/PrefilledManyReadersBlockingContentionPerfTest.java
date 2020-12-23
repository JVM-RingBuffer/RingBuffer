package test.object;

import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyReadersBlockingContentionPerfTest extends PrefilledManyReadersBlockingContentionTest {
    public static final PrefilledRingBuffer2<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingContentionPerfTest().runBenchmark();
    }

    PrefilledRingBuffer2<Event> getRingBuffer() {
        return RING_BUFFER;
    }
}
