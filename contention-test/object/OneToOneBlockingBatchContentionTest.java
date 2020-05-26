package test.object;

import test.AbstractRingBufferTest;

class OneToOneBlockingBatchContentionTest extends OneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startAsync(AbstractRingBufferTest.NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.NUM_ITERATIONS, RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
