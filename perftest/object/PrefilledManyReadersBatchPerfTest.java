package test.object;

class PrefilledManyReadersBatchPerfTest extends PrefilledManyReadersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
