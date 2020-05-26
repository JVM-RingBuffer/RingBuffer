package test.object;

import test.AbstractRingBufferTest;

class PrefilledManyReadersBatchContentionTest extends PrefilledManyReadersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
