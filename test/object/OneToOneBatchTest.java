package test.object;

import test.AbstractRingBufferTest;

class OneToOneBatchTest extends OneToOneTest {
    public static void main(String[] args) {
        new OneToOneBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startAsync(AbstractRingBufferTest.NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.NUM_ITERATIONS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
