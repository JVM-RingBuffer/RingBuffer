package test.marshalling;

import test.Profiler;

class ManyReadersDirectMarshallingTest extends ManyReadersDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyReadersDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return DirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
