package test;

class OneToOneBatchPerfTest extends OneToOnePerfTest {
    public static void main(String[] args) {
        new OneToOneBatchPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
