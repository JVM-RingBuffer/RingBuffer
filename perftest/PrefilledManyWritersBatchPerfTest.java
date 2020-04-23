package test;

class PrefilledManyWritersBatchPerfTest extends PrefilledManyWritersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
