package test.marshalling;

class OneToOneMarshallingTest extends OneToOneMarshallingContentionTest {
    public static void main(String[] args) {
        new OneToOneMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
