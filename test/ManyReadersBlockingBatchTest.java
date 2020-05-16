package test;

class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
