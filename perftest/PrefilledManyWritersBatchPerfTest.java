package test;

class PrefilledManyWritersBatchPerfTest extends PrefilledManyWritersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        OverwritingPrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
