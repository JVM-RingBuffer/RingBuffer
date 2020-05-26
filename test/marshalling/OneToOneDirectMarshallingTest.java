package test.marshalling;

import test.Profiler;

class OneToOneDirectMarshallingTest extends OneToOneDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new OneToOneDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        DirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
