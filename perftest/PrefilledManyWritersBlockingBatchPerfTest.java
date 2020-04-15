package test;

class PrefilledManyWritersBlockingBatchPerfTest extends PrefilledManyWritersBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return AdvancingBatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_SIZE, RING_BUFFER);
    }
}
