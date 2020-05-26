package test.marshalling;

import test.Profiler;

class OneToOneMarshallingBlockingTest extends OneToOneMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        BlockingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
