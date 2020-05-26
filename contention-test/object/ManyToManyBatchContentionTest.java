package test.object;

class ManyToManyBatchContentionTest extends ManyToManyContentionTest {
    public static void main(String[] args) {
        new ManyToManyBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
