package test.object;

import test.AbstractRingBufferTest;

class PrefilledManyReadersBlockingBatchContentionTest extends PrefilledManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
