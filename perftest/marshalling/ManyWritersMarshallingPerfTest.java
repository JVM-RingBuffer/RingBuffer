package test.marshalling;

class ManyWritersMarshallingPerfTest extends ManyWritersMarshallingTest {
    public static void main(String[] args) {
        new ManyWritersMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
