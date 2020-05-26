package test.object;

class PrefilledManyToManyBlockingBatchContentionTest extends PrefilledManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
