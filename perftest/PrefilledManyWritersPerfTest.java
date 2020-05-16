package test;

class PrefilledManyWritersPerfTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersPerfTest().runBenchmark();
    }

    @Override
    long testSum() {
        OverwritingPrefilledWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
