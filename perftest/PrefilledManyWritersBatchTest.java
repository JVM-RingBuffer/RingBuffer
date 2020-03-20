package perftest;

class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runTest();
    }

    @Override
    public long run() {
        BatchReader reader = BatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_SIZE, RING_BUFFER);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
