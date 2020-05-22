package test;

class PrefilledManyToManyBatchPerfTest extends PrefilledManyToManyPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
