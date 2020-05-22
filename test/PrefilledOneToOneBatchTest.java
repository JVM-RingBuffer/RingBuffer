package test;

class PrefilledOneToOneBatchTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledOverwritingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
