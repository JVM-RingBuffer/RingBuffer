package test.marshalling;

class ManyWritersMarshallingBlockingPerfTest extends ManyWritersMarshallingBlockingTest {
    public static void main(String[] args) {
        new ManyWritersMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runGroupAsync(RING_BUFFER);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
