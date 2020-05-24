package test;

class PrefilledManyWritersBatchPerfTest extends PrefilledManyWritersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
