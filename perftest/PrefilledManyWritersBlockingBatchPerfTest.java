package test;

class PrefilledManyWritersBlockingBatchPerfTest extends PrefilledManyWritersBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
