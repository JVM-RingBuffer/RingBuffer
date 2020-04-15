package test;

class PrefilledManyWritersPerfTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
