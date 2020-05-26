package test.object;

import test.Profiler;

class ManyWritersBatchContentionTest extends ManyWritersContentionTest {
    public static void main(String[] args) {
        new ManyWritersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
