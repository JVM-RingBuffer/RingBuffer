package test.marshalling;

import test.Profiler;

class OneToOneMarshallingTest extends OneToOneMarshallingContentionTest {
    public static void main(String[] args) {
        new OneToOneMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
