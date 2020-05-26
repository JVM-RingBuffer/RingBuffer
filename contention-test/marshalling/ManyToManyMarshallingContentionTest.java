package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;

public class ManyToManyMarshallingContentionTest extends RingBufferTest {
    public static final MarshallingRingBuffer RING_BUFFER =
            MarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyToManyMarshallingContentionTest().runBenchmark();
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
        Writer.startGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
