package test.marshalling;

class ManyToManyDirectMarshallingPerfTest extends ManyToManyDirectMarshallingTest {
    public static void main(String[] args) {
        new ManyToManyDirectMarshallingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runGroupAsync(RING_BUFFER);
        return DirectReader.runGroupAsync(RING_BUFFER);
    }
}
