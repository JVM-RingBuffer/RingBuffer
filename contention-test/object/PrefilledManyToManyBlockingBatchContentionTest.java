package test.object;

import test.Profiler;

class PrefilledManyToManyBlockingBatchContentionTest extends PrefilledManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
