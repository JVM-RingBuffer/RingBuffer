package test.marshalling;

class OneToOneMarshallingPerfTest extends OneToOneMarshallingTest {
    public static void main(String[] args) {
        new OneToOneMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
