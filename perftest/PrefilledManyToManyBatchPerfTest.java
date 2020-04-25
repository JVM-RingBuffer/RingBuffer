package test;

class PrefilledManyToManyBatchPerfTest extends PrefilledManyToManyPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
