package test.object;

class ManyWritersBatchTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
