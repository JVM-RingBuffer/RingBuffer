package test.object;

import test.Profiler;

class ManyToManyTest extends ManyToManyContentionTest {
    public static void main(String[] args) {
        new ManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
