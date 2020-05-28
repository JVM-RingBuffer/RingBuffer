package test.marshalling;

import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.Profiler;

public class ManyWritersDirectMarshallingContentionTest extends RingBufferTest {
    public static final DirectMarshallingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyWritersDirectMarshallingContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 12;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectWriter.startGroupAsync(RING_BUFFER, profiler);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}