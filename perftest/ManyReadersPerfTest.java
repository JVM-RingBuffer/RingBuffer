package test;

class ManyReadersPerfTest extends ManyReadersTest {
    public static void main(String[] args) {
        new ManyReadersPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
