package test.object;

import test.Profiler;

class ManyToManyBatchContentionTest extends ManyToManyContentionTest {
    public static void main(String[] args) {
        new ManyToManyBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
