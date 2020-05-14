package test;

class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = OverwritingPrefilledWriter.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
