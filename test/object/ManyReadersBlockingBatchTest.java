package test.object;

import test.AbstractRingBufferTest;

class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
