package test.marshalling;

class ManyWritersDirectMarshallingPerfTest extends ManyWritersDirectMarshallingTest {
    public static void main(String[] args) {
        new ManyWritersDirectMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runGroupAsync(RING_BUFFER);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
