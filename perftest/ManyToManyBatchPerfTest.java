package test;

class ManyToManyBatchPerfTest extends ManyToManyPerfTest {
    public static void main(String[] args) {
        new ManyToManyBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
