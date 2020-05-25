package test.object;

class ManyWritersPerfTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
