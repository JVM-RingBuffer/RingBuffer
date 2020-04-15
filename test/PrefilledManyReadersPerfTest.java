package test;

class PrefilledManyReadersPerfTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
