package test.marshalling;

import test.Profiler;

class ManyWritersMarshallingTest extends ManyWritersMarshallingContentionTest {
    public static void main(String[] args) {
        new ManyWritersMarshallingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
