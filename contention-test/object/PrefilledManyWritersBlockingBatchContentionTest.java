package test.object;

import test.AbstractRingBufferTest;

class PrefilledManyWritersBlockingBatchContentionTest extends PrefilledManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
