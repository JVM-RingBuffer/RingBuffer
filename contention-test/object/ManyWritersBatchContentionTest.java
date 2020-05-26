package test.object;

import test.AbstractRingBufferTest;

class ManyWritersBatchContentionTest extends ManyWritersContentionTest {
    public static void main(String[] args) {
        new ManyWritersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
