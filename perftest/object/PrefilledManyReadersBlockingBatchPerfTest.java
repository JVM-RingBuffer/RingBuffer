package test.object;

class PrefilledManyReadersBlockingBatchPerfTest extends PrefilledManyReadersBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
