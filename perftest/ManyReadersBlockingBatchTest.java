package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class ManyReadersBlockingBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        final RingBuffer<Event> ringBuffer = ManyReadersBlockingTest.RING_BUFFER;
        TestThreadGroup readerGroup = BatchReader.newGroup(ringBuffer);
        Writer writer = new Writer(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
