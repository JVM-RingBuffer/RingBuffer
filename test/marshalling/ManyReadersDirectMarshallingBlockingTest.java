package test.marshalling;

import test.Profiler;

class ManyReadersDirectMarshallingBlockingTest extends ManyReadersDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectBlockingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
