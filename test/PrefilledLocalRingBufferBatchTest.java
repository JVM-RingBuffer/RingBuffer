package test;

class PrefilledLocalRingBufferBatchTest extends PrefilledLocalRingBufferTest {
    public static void main(String[] args) {
        new PrefilledLocalRingBufferBatchTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runSync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runSync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
