package test.marshalling;

class ManyReadersDirectMarshallingBlockingPerfTest extends ManyReadersDirectMarshallingBlockingTest {
    public static void main(String[] args) {
        new ManyReadersDirectMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER);
    }
}
