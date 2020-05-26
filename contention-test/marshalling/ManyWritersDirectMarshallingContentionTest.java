package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

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
        DirectWriter.startGroupAsync(RING_BUFFER);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
