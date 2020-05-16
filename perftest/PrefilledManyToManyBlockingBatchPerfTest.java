package test;

class PrefilledManyToManyBlockingBatchPerfTest extends PrefilledManyToManyBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
