package test.marshalling;

class ManyWritersMarshallingBlockingTest extends ManyWritersMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runGroupAsync(RING_BUFFER);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
