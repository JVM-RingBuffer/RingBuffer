package test.object;

import test.Profiler;

class ManyReadersBlockingBatchContentionTest extends ManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
