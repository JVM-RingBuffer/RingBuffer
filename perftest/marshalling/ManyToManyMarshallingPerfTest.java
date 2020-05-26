package test.marshalling;

class ManyToManyMarshallingPerfTest extends ManyToManyMarshallingTest {
    public static void main(String[] args) {
        new ManyToManyMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
