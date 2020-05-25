package test.object;

class ManyToManyPerfTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
