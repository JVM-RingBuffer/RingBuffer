package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class ManyReadersDirectMarshallingContentionTest extends RingBufferTest {
    public static final DirectMarshallingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new ManyReadersDirectMarshallingContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 40;
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        DirectWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return DirectReader.runGroupAsync(RING_BUFFER);
    }
}
