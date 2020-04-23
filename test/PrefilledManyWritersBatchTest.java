package test;

class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = PrefilledSynchronizedWriter.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
