package test;

class ManyReadersBatchPerfTest extends ManyReadersPerfTest {
    public static void main(String[] args) {
        new ManyReadersBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
    }
}
