package test.marshalling;

import test.Profiler;

class ManyReadersMarshallingBlockingTest extends ManyReadersMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        BlockingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return BlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
