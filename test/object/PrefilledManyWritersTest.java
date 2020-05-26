package test.object;

class PrefilledManyWritersTest extends PrefilledManyWritersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
