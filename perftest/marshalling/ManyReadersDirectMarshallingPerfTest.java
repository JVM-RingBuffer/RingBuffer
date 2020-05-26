package test.marshalling;

class ManyReadersDirectMarshallingPerfTest extends ManyReadersDirectMarshallingTest {
    public static void main(String[] args) {
        new ManyReadersDirectMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return DirectReader.runGroupAsync(RING_BUFFER);
    }
}
