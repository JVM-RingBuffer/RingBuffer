package test.object;

import test.Profiler;

class PrefilledManyReadersBlockingBatchContentionTest extends PrefilledManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
