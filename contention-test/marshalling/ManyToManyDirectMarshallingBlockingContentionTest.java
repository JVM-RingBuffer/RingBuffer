package test.marshalling;

import org.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.Profiler;

public class ManyToManyDirectMarshallingBlockingContentionTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyDirectMarshallingBlockingContentionTest().runBenchmark();
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
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectBlockingWriter.startGroupAsync(RING_BUFFER, profiler);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
