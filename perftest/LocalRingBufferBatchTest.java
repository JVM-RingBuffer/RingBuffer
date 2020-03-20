package perftest;

class LocalRingBufferBatchTest extends LocalRingBufferTest {
    public static void main(String[] args) {
        new LocalRingBufferBatchTest().runTest();
    }

    @Override
    public long run() {
        Writer writer = Writer.runSync(NUM_ITERATIONS, RING_BUFFER);
        BatchReader reader = BatchReader.runSync(NUM_ITERATIONS, RING_BUFFER);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
