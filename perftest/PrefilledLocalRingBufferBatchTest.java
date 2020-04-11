package perftest;

class PrefilledLocalRingBufferBatchTest extends PrefilledLocalRingBufferTest {
    public static void main(String[] args) {
        new PrefilledLocalRingBufferBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runSync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runSync(NUM_ITERATIONS, READ_BUFFER_SIZE, RING_BUFFER);
    }
}
