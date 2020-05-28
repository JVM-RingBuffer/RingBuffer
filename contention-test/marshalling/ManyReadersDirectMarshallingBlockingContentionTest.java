package test.marshalling;

import org.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.Profiler;

public class ManyReadersDirectMarshallingBlockingContentionTest extends RingBufferTest {
    public static final DirectMarshallingBlockingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersDirectMarshallingBlockingContentionTest().runBenchmark();
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
        DirectBlockingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
