package test.object;

import test.AbstractRingBufferTest;

class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startGroupAsync(RING_BUFFER);
        return BatchReader.runAsync(AbstractRingBufferTest.TOTAL_ELEMENTS, RingBufferTest.BATCH_SIZE, RING_BUFFER);
    }
}
