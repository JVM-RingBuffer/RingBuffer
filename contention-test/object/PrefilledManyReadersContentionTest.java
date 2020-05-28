package test.object;

import org.ringbuffer.object.PrefilledOverwritingRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import test.Profiler;

public class PrefilledManyReadersContentionTest extends RingBufferTest {
    public static final PrefilledOverwritingRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 34;
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledOverwritingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
