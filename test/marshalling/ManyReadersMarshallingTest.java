package test.marshalling;

class ManyReadersMarshallingTest extends ManyReadersMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyReadersMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
