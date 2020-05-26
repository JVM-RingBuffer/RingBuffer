package test.object;

class ManyReadersTest extends ManyReadersContentionTest {
    public static void main(String[] args) {
        new ManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
