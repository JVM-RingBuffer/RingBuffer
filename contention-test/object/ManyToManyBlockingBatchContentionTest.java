package test.object;

class ManyToManyBlockingBatchContentionTest extends ManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
