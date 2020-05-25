package test.object;

import test.AbstractRingBufferTest;

class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startAsync(AbstractRingBufferTest.NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.NUM_ITERATIONS, RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
