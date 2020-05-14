package test;

class PrefilledManyReadersBatchPerfTest extends PrefilledManyReadersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchPerfTest().run();
    }

    @Override
    long testSum() {
        OverwritingPrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
