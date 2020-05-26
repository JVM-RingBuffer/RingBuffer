package test.object;

import test.Profiler;

class ManyWritersTest extends ManyWritersContentionTest {
    public static void main(String[] args) {
        new ManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
