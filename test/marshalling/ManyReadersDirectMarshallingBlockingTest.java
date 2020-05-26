package test.marshalling;

class ManyReadersDirectMarshallingBlockingTest extends ManyReadersDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER);
    }
}
