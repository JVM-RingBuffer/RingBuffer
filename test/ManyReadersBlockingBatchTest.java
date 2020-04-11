package test;

class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer writer = Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = BatchReader.runGroupAsync(READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
