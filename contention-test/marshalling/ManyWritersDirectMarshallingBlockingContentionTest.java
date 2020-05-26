package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class ManyWritersDirectMarshallingBlockingContentionTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersDirectMarshallingBlockingContentionTest().runBenchmark();
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
        return DirectBlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
