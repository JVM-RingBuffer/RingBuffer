package test.marshalling;

class OneToOneDirectMarshallingBlockingPerfTest extends OneToOneDirectMarshallingBlockingTest {
    public static void main(String[] args) {
        new OneToOneDirectMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return DirectBlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
