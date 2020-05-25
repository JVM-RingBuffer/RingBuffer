package test.object;

class ManyWritersBatchPerfTest extends ManyWritersPerfTest {
    public static void main(String[] args) {
        new ManyWritersBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
