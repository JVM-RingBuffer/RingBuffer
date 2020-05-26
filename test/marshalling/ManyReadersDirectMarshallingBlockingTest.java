package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class ManyReadersDirectMarshallingBlockingTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersDirectMarshallingBlockingTest().runBenchmark();
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
        DirectBlockingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER);
    }
}
