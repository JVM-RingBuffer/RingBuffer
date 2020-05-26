package test.object;

class PrefilledManyToManyBatchContentionTest extends PrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
