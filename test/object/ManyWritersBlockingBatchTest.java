package test.object;

class ManyWritersBlockingBatchTest extends ManyWritersBlockingTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
