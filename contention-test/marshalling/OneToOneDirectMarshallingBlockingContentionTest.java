package test.marshalling;

import org.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.Profiler;

public class OneToOneDirectMarshallingBlockingContentionTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(BLOCKING_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new OneToOneDirectMarshallingBlockingContentionTest().runBenchmark();
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
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        DirectBlockingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectBlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
