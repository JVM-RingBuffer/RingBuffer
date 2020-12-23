package test.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

class PrefilledManyToManyBlockingBatchContentionPerfTest extends PrefilledManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledManyToManyBlockingContentionPerfTest.RING_BUFFER;
    }
}
