package test;

class PrefilledManyReadersPerfTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
