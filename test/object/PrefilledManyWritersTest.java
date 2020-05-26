package test.object;

import test.Profiler;

class PrefilledManyWritersTest extends PrefilledManyWritersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
