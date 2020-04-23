package test;

class OneToOneBlockingBatchTest extends OneToOneBlockingTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer writer = Writer.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
