package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class ManyToManyDirectMarshallingTest extends RingBufferTest {
    public static final DirectMarshallingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyToManyDirectMarshallingTest().runBenchmark();
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
        return DirectReader.runGroupAsync(RING_BUFFER);
    }
}
