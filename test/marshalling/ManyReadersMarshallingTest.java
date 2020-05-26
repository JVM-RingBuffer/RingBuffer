package test.marshalling;

import test.Profiler;

class ManyReadersMarshallingTest extends ManyReadersMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyReadersMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
