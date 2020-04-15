package test;

class ManyReadersBatchTest extends ManyReadersTest {
    public static void main(String[] args) {
        new ManyReadersBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer writer = Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
