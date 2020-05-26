package test.marshalling;

class OneToOneDirectMarshallingTest extends OneToOneDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new OneToOneDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return DirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
