package test.marshalling;

class OneToOneMarshallingBlockingTest extends OneToOneMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BlockingReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
