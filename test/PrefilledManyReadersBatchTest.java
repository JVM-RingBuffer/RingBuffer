package test;

class PrefilledManyReadersBatchTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
