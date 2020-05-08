package test;

class OneToOneBatchTest extends OneToOneTest {
    public static void main(String[] args) {
        new OneToOneBatchTest().run();
    }

    @Override
    long testSum() {
        Writer writer = Writer.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
