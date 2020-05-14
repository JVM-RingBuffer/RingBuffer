package test;

class PrefilledManyReadersBatchTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchTest().run();
    }

    @Override
    long testSum() {
        OverwritingPrefilledWriter writer = OverwritingPrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
