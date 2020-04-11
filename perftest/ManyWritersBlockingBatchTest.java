package perftest;

class ManyWritersBlockingBatchTest extends ManyWritersBlockingTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
    }
}
