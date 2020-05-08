package test;

class ManyWritersBlockingBatchTest extends ManyWritersBlockingTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
