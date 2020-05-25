package test.object;

class ManyToManyBlockingBatchTest extends ManyToManyBlockingTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
