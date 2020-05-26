package test.marshalling;

import test.Profiler;

class ManyWritersDirectMarshallingBlockingTest extends ManyWritersDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectBlockingWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectBlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
