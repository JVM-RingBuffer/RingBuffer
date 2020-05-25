package test.object;

class PrefilledManyToManyBlockingBatchTest extends PrefilledManyToManyBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
