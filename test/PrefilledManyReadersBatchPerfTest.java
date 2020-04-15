package test;

class PrefilledManyReadersBatchPerfTest extends PrefilledManyReadersPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
    }
}
