package test.object;

class ManyToManyBlockingBatchTest extends ManyToManyBlockingTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
