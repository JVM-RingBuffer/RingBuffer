package test.object;

class PrefilledOneToOneTest extends PrefilledOneToOneContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
