package perftest;

class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = BatchReader.runGroupAsync(READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
        Writer writer = Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
