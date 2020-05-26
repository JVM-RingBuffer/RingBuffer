package test.marshalling;

class ManyToManyMarshallingBlockingPerfTest extends ManyToManyMarshallingBlockingTest {
    public static void main(String[] args) {
        new ManyToManyMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        BlockingWriter.runGroupAsync(RING_BUFFER);
        return BlockingReader.runGroupAsync(RING_BUFFER);
    }
}
