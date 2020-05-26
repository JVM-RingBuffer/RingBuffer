package test.marshalling;

class ManyReadersMarshallingBlockingTest extends ManyReadersMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BlockingReader.runGroupAsync(RING_BUFFER);
    }
}
