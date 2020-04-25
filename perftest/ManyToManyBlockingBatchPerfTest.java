package test;

class ManyToManyBlockingBatchPerfTest extends ManyToManyBlockingPerfTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
