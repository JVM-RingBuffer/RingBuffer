package test;

class OneToOneBatchTest extends OneToOneTest {
    public static void main(String[] args) {
        new OneToOneBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer writer = Writer.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = BatchReader.runAsync(NUM_ITERATIONS, READ_BUFFER_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
