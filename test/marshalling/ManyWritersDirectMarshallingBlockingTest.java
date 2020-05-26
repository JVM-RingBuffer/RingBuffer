package test.marshalling;

class ManyWritersDirectMarshallingBlockingTest extends ManyWritersDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runGroupAsync(RING_BUFFER);
        return DirectBlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
