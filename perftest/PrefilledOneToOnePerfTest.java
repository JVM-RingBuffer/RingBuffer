package test;

class PrefilledOneToOnePerfTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOnePerfTest().run();
    }

    @Override
    long testSum() {
        OverwritingPrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
