package perftest;

class ManyReadersBlockingBatchPerfTest extends ManyReadersBlockingPerfTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
    }
}
