package test.object;

class PrefilledOneToOneBatchTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
