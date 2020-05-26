package test.object;

import test.Profiler;

class ManyReadersTest extends ManyReadersContentionTest {
    public static void main(String[] args) {
        new ManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
