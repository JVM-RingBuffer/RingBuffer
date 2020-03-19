package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledManyWritersBlockingBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        final RingBuffer<Event> ringBuffer = PrefilledManyWritersBlockingTest.RING_BUFFER;
        DisposingBatchReader reader = new DisposingBatchReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
