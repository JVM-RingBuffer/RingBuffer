package test.object;

class ManyWritersTest extends ManyWritersContentionTest {
    public static void main(String[] args) {
        new ManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
