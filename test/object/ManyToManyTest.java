package test.object;

class ManyToManyTest extends ManyToManyContentionTest {
    public static void main(String[] args) {
        new ManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
