package test.object;

class PrefilledManyReadersBlockingBatchTest extends PrefilledManyReadersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
