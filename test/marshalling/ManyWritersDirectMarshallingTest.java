package test.marshalling;

import test.Profiler;

class ManyWritersDirectMarshallingTest extends ManyWritersDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyWritersDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
