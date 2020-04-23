package test;

class PrefilledManyReadersBatchPerfTest extends PrefilledManyReadersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
