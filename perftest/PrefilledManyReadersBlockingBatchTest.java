package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledManyReadersBlockingBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 12;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        final RingBuffer<Event> ringBuffer = PrefilledManyReadersBlockingTest.RING_BUFFER;
        TestThreadGroup readerGroup = SynchronizedDisposingBatchReader.newGroup(ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
