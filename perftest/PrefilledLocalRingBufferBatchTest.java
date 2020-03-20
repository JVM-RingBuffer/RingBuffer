package perftest;

class PrefilledLocalRingBufferBatchTest extends PrefilledLocalRingBufferTest {
    public static void main(String[] args) {
        new PrefilledLocalRingBufferBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.runSync(NUM_ITERATIONS, RING_BUFFER);
        BatchReader reader = BatchReader.runSync(NUM_ITERATIONS, RING_BUFFER);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
