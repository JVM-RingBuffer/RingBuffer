package test.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyWritersBlockingBatchContentionPerfTest extends PrefilledManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledManyWritersBlockingContentionPerfTest.RING_BUFFER;
    }
}
