package test;

class ManyReadersBlockingBatchPerfTest extends ManyReadersBlockingPerfTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
