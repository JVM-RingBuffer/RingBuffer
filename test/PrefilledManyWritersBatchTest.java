package test;

class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = OverwritingPrefilledWriter.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
