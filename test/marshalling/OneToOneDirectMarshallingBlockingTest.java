package test.marshalling;

class OneToOneDirectMarshallingBlockingTest extends OneToOneDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return DirectBlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
