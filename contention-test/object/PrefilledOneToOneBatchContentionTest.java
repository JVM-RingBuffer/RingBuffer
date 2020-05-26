package test.object;

import test.AbstractRingBufferTest;

class PrefilledOneToOneBatchContentionTest extends PrefilledOneToOneContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startAsync(AbstractRingBufferTest.NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.NUM_ITERATIONS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
