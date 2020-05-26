package test.object;

import test.AbstractRingBufferTest;

class OneToOneBatchContentionTest extends OneToOneContentionTest {
    public static void main(String[] args) {
        new OneToOneBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startAsync(AbstractRingBufferTest.NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.NUM_ITERATIONS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
