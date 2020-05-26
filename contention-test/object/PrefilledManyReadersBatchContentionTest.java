package test.object;

import test.Profiler;

class PrefilledManyReadersBatchContentionTest extends PrefilledManyReadersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        PrefilledOverwritingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
