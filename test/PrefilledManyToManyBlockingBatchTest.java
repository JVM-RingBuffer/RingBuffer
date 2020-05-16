package test;

class PrefilledManyToManyBlockingBatchTest extends PrefilledManyToManyBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
