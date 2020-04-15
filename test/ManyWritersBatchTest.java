package test;

class ManyWritersBatchTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
