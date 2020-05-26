package test.object;

class PrefilledManyReadersTest extends PrefilledManyReadersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
