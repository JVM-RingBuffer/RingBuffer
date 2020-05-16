package test;

class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().runBenchmark();
    }

    @Override
    long testSum() {
        OverwritingPrefilledWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
