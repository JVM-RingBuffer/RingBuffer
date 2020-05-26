package test.object;

class PrefilledManyReadersBatchTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
