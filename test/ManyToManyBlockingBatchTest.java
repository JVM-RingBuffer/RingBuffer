package test;

class ManyToManyBlockingBatchTest extends ManyToManyBlockingTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
