package test.object;

import test.AbstractRingBufferTest;

class ManyWritersBatchTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
