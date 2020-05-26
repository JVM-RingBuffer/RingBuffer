package test.marshalling;

class ManyWritersMarshallingTest extends ManyWritersMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyWritersMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
