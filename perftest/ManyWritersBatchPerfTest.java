package test;

class ManyWritersBatchPerfTest extends ManyWritersPerfTest {
    public static void main(String[] args) {
        new ManyWritersBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
