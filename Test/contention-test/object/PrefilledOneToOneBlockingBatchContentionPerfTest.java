package test.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledOneToOneBlockingBatchContentionPerfTest extends PrefilledOneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledOneToOneBlockingContentionPerfTest.RING_BUFFER;
    }
}
