package test.object;

import test.Profiler;

class ManyWritersBlockingBatchContentionTest extends ManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
