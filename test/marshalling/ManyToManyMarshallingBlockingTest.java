package test.marshalling;

class ManyToManyMarshallingBlockingTest extends ManyToManyMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runGroupAsync(RING_BUFFER);
        return BlockingReader.runGroupAsync(RING_BUFFER);
    }
}
