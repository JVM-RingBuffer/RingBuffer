package test.object;

import test.AbstractRingBufferTest;

class PrefilledOneToOneBlockingBatchContentionTest extends PrefilledOneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startAsync(AbstractRingBufferTest.NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.NUM_ITERATIONS, RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
