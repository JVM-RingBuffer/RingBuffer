package test;

class OneToOnePerfTest extends OneToOneTest {
    public static void main(String[] args) {
        new OneToOnePerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
