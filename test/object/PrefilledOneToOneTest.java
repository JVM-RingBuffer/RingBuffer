package test.object;

import test.Profiler;

class PrefilledOneToOneTest extends PrefilledOneToOneContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        PrefilledOverwritingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
