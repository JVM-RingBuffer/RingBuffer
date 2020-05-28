package test.marshalling;

import org.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.MarshallingRingBuffer;
import test.Profiler;

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
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        BlockingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
