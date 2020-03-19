package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class ManyWritersBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new ManyWritersBatchTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 12;
    }

    @Override
    public long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        final RingBuffer<Event> ringBuffer = ManyWritersTest.RING_BUFFER;
        BatchReader reader = new BatchReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = Writer.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
