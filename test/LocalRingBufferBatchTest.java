package test;

class LocalRingBufferBatchTest extends LocalRingBufferTest {
    public static void main(String[] args) {
        new LocalRingBufferBatchTest().run();
    }

    @Override
    long testSum() {
        Writer.runSync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runSync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
