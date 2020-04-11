package perftest;

class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
    }
}
