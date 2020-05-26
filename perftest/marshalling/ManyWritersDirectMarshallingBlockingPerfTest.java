package test.marshalling;

class ManyWritersDirectMarshallingBlockingPerfTest extends ManyWritersDirectMarshallingBlockingTest {
    public static void main(String[] args) {
        new ManyWritersDirectMarshallingBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        DirectBlockingWriter.runGroupAsync(RING_BUFFER);
        return DirectBlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
