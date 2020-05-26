package test.marshalling;

import test.Profiler;

class ManyToManyDirectMarshallingTest extends ManyToManyDirectMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyToManyDirectMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
