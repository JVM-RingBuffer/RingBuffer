package perftest;

class OneToOneBlockingBatchTest extends OneToOneBlockingTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
    }
}
