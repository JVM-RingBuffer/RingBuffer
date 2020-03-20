package perftest;

class PrefilledManyReadersBatchTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
        PrefilledWriter writer = PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
