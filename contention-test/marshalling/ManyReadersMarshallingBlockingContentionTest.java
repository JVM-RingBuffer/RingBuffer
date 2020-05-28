package test.marshalling;

import org.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.MarshallingRingBuffer;
import test.Profiler;

public class ManyReadersMarshallingBlockingContentionTest extends RingBufferTest {
    public static final MarshallingBlockingRingBuffer RING_BUFFER =
            MarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersMarshallingBlockingContentionTest().runBenchmark();
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
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        BlockingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return BlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}