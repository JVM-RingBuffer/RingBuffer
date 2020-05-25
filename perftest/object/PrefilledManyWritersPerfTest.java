package test.object;

class PrefilledManyWritersPerfTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
