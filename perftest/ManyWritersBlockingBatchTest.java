package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class ManyWritersBlockingBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchTest().runTest();
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
        final RingBuffer<Event> ringBuffer = ManyWritersBlockingTest.RING_BUFFER;
        BatchReader reader = new BatchReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = Writer.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
