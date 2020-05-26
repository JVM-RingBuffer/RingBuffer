package test.object;

import test.AbstractRingBufferTest;

class ManyWritersBlockingBatchContentionTest extends ManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
