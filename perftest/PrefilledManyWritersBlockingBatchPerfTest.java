package test;

class PrefilledManyWritersBlockingBatchPerfTest extends PrefilledManyWritersBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
