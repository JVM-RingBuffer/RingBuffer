package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledManyWritersBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runTest();
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
        final RingBuffer<Event> ringBuffer = PrefilledManyWritersTest.RING_BUFFER;
        BatchReader reader = new BatchReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
