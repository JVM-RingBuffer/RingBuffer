package test.marshalling;

class OneToOneDirectMarshallingPerfTest extends OneToOneDirectMarshallingTest {
    public static void main(String[] args) {
        new OneToOneDirectMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return DirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
