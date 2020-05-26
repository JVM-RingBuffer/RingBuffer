package test.object;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
