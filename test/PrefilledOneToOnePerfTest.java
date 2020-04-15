package test;

class PrefilledOneToOnePerfTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOnePerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
