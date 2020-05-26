package test.object;

import test.AbstractRingBufferTest;

class ManyReadersBlockingBatchContentionTest extends ManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
