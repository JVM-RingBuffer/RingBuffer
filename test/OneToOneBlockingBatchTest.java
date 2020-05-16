package test;

class OneToOneBlockingBatchTest extends OneToOneBlockingTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        Writer.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
