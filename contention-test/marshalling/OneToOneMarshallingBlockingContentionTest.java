package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;

public class OneToOneMarshallingBlockingContentionTest extends RingBufferTest {
    public static final MarshallingBlockingRingBuffer RING_BUFFER =
            MarshallingRingBuffer.withCapacity(BLOCKING_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneMarshallingBlockingContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 50;
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        BlockingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return BlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
