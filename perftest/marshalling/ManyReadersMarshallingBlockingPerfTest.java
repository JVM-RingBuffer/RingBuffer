package test.marshalling;

class ManyReadersMarshallingBlockingPerfTest extends ManyReadersMarshallingBlockingTest {
    public static void main(String[] args) {
        new ManyReadersMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return BlockingReader.runGroupAsync(RING_BUFFER);
    }
}
