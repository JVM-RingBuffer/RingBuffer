package test.object;

import test.Profiler;

class ManyToManyBlockingBatchContentionTest extends ManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
