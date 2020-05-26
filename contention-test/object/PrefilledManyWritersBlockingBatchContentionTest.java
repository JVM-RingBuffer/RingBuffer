package test.object;

import test.Profiler;

class PrefilledManyWritersBlockingBatchContentionTest extends PrefilledManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
