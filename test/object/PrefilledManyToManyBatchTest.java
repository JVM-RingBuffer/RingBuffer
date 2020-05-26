package test.object;

import test.Profiler;

class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledOverwritingWriter.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
