package test.marshalling;

import test.Profiler;

class ManyToManyMarshallingTest extends ManyToManyMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyToManyMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
