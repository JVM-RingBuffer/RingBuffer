package test.object;

import test.Profiler;

class PrefilledManyToManyBatchContentionTest extends PrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledOverwritingWriter.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
