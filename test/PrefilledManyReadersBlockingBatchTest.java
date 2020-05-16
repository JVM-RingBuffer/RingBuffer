package test;

class PrefilledManyReadersBlockingBatchTest extends PrefilledManyReadersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
