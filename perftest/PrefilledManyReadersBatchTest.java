package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledManyReadersBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 34;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        final RingBuffer<Event> ringBuffer = PrefilledManyReadersTest.RING_BUFFER;
        TestThreadGroup readerGroup = BatchReader.newGroup(ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
