package test.marshalling;

import test.Profiler;

class ManyWritersMarshallingBlockingTest extends ManyWritersMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(RING_BUFFER, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
