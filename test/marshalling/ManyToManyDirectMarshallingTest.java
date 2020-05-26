package test.marshalling;

class ManyToManyDirectMarshallingTest extends ManyToManyDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyToManyDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectWriter.runGroupAsync(RING_BUFFER);
        return DirectReader.runGroupAsync(RING_BUFFER);
    }
}
