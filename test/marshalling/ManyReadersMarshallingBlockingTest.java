package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;

public class ManyReadersMarshallingBlockingTest extends RingBufferTest {
    public static final MarshallingBlockingRingBuffer RING_BUFFER =
            MarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        BlockingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BlockingReader.runGroupAsync(RING_BUFFER);
    }
}
