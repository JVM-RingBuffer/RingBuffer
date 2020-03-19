package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class OneToOneBlockingBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 50;
    }

    @Override
    public long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    public long run() {
        final RingBuffer<Event> ringBuffer = OneToOneBlockingTest.RING_BUFFER;
        BatchReader reader = new BatchReader(NUM_ITERATIONS, ringBuffer);
        Writer writer = new Writer(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
