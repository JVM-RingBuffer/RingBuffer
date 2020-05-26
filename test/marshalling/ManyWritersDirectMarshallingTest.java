package test.marshalling;

class ManyWritersDirectMarshallingTest extends ManyWritersDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyWritersDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runGroupAsync(RING_BUFFER);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
