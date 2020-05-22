package test;

class PrefilledManyToManyPerfTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
