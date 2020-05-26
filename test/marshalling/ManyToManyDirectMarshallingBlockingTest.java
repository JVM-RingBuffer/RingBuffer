package test.marshalling;

class ManyToManyDirectMarshallingBlockingTest extends ManyToManyDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runGroupAsync(RING_BUFFER);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER);
    }
}
