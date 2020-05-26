package test.marshalling;

class ManyToManyMarshallingTest extends ManyToManyMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyToManyMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
