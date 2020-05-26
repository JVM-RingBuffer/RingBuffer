package test.marshalling;

import test.Profiler;

class OneToOneDirectMarshallingBlockingTest extends OneToOneDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        DirectBlockingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectBlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
