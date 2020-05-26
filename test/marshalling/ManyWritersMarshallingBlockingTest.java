package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;

public class ManyWritersMarshallingBlockingTest extends RingBufferTest {
    public static final MarshallingBlockingRingBuffer RING_BUFFER =
            MarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersMarshallingBlockingTest().runBenchmark();
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
        BlockingWriter.startGroupAsync(RING_BUFFER);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
