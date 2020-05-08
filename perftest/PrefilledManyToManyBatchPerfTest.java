package test;

class PrefilledManyToManyBatchPerfTest extends PrefilledManyToManyPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
