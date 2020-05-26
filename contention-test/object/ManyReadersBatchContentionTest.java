package test.object;

import test.Profiler;

class ManyReadersBatchContentionTest extends ManyReadersContentionTest {
    public static void main(String[] args) {
        new ManyReadersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
