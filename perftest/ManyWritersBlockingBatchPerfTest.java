package test;

class ManyWritersBlockingBatchPerfTest extends ManyWritersBlockingPerfTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER);
    }
}
