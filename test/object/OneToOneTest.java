package test.object;

class OneToOneTest extends OneToOneContentionTest {
    public static void main(String[] args) {
        new OneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
