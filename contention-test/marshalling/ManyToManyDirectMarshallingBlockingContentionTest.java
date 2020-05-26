package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class ManyToManyDirectMarshallingBlockingContentionTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyDirectMarshallingBlockingContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.startGroupAsync(RING_BUFFER);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER);
    }
}
