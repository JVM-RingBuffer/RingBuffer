package test;

class ManyReadersBlockingBatchPerfTest extends ManyReadersBlockingPerfTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
    }
}
