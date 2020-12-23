package test.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyReadersBlockingBatchContentionPerfTest extends PrefilledManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledManyReadersBlockingContentionPerfTest.RING_BUFFER;
    }
}
