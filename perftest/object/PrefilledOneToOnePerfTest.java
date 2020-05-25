package test.object;

class PrefilledOneToOnePerfTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOnePerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
