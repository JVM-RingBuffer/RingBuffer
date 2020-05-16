package test;

class ManyReadersPerfTest extends ManyReadersTest {
    public static void main(String[] args) {
        new ManyReadersPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
