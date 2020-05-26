package test.object;

class PrefilledManyToManyTest extends PrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
