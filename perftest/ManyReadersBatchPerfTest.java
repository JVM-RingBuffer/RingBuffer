package test;

class ManyReadersBatchPerfTest extends ManyReadersPerfTest {
    public static void main(String[] args) {
        new ManyReadersBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
