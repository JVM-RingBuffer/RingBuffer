package test;

class PrefilledManyToManyBlockingBatchPerfTest extends PrefilledManyToManyBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
