package test;

class ManyToManyBatchTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyBatchTest().run();
    }

    @Override
    long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
