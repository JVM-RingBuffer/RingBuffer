package test.object;

class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
