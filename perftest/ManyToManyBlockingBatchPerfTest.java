package test;

class ManyToManyBlockingBatchPerfTest extends ManyToManyBlockingPerfTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}