package test;

class OneToOnePerfTest extends OneToOneTest {
    public static void main(String[] args) {
        new OneToOnePerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
