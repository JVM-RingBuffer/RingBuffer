package test.marshalling;

import test.Profiler;

class ManyToManyMarshallingBlockingTest extends ManyToManyMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(RING_BUFFER, profiler);
        return BlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
