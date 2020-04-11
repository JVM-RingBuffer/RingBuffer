package perftest;

class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledKeyedWriter.runGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_SIZE, RING_BUFFER);
    }
}
