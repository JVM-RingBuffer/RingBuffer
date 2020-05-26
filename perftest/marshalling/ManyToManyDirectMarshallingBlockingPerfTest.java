package test.marshalling;

class ManyToManyDirectMarshallingBlockingPerfTest extends ManyToManyDirectMarshallingBlockingTest {
    public static void main(String[] args) {
        new ManyToManyDirectMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runGroupAsync(RING_BUFFER);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER);
    }
}
