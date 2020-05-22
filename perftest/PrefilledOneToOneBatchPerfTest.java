package test;

class PrefilledOneToOneBatchPerfTest extends PrefilledOneToOnePerfTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledOverwritingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
