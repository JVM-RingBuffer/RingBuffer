package test.object;

class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
