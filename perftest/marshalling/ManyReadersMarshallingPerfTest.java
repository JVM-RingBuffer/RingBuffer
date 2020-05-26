package test.marshalling;

class ManyReadersMarshallingPerfTest extends ManyReadersMarshallingTest {
    public static void main(String[] args) {
        new ManyReadersMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
