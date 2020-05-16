package test;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
