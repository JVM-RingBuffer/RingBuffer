package test;

class PrefilledManyReadersBlockingBatchPerfTest extends PrefilledManyReadersBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
