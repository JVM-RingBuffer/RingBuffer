package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;

public class OneToOneDirectMarshallingTest extends RingBufferTest {
    public static final DirectMarshallingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new OneToOneDirectMarshallingTest().runBenchmark();
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
        DirectWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return DirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
