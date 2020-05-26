package test.object;

class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
