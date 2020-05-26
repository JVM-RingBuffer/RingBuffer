package test.marshalling;

class OneToOneMarshallingBlockingPerfTest extends OneToOneMarshallingBlockingTest {
    public static void main(String[] args) {
        new OneToOneMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
