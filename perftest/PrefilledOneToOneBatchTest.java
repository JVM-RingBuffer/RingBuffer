package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledOneToOneBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchTest().runTest();
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
        final RingBuffer<Event> ringBuffer = PrefilledOneToOneTest.RING_BUFFER;
        BatchReader reader = new BatchReader(NUM_ITERATIONS, ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
