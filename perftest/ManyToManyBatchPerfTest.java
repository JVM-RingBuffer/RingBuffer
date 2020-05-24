package test;

class ManyToManyBatchPerfTest extends ManyToManyPerfTest {
    public static void main(String[] args) {
        new ManyToManyBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
