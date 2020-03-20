package perftest;

class ManyReadersBatchTest extends ManyReadersTest {
    public static void main(String[] args) {
        new ManyReadersBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
        Writer writer = Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
