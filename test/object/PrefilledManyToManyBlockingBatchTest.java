package test.object;

class PrefilledManyToManyBlockingBatchTest extends PrefilledManyToManyBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
