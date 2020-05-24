package test;

class PrefilledManyToManyBlockingBatchPerfTest extends PrefilledManyToManyBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
