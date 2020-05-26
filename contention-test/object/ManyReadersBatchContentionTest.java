package test.object;

import test.AbstractRingBufferTest;

class ManyReadersBatchContentionTest extends ManyReadersContentionTest {
    public static void main(String[] args) {
        new ManyReadersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
