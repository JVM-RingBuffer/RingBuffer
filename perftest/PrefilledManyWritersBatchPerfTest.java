package test;

class PrefilledManyWritersBatchPerfTest extends PrefilledManyWritersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
