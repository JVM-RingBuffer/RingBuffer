package test.marshalling;

import test.Profiler;

class ManyToManyDirectMarshallingBlockingTest extends ManyToManyDirectMarshallingBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyDirectMarshallingBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        DirectBlockingWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectBlockingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
