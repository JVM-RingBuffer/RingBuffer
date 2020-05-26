package test.marshalling;

class ManyReadersDirectMarshallingTest extends ManyReadersDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyReadersDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return DirectReader.runGroupAsync(RING_BUFFER);
    }
}
