package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;

public class ManyReadersMarshallingContentionTest extends RingBufferTest {
    public static final MarshallingRingBuffer RING_BUFFER =
            MarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new ManyReadersMarshallingContentionTest().runBenchmark();
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
        Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
