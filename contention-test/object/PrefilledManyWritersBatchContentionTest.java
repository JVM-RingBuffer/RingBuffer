package test.object;

import test.AbstractRingBufferTest;

class PrefilledManyWritersBatchContentionTest extends PrefilledManyWritersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
