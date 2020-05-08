package test;

class ManyWritersBatchTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
