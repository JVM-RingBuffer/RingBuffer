package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class OneToOneDirectMarshallingBlockingTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(BLOCKING_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneDirectMarshallingBlockingTest().runBenchmark();
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
        DirectBlockingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return DirectBlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
