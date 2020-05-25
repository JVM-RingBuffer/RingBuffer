package test.object;

class ManyToManyBatchTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runGroupAsync(RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
