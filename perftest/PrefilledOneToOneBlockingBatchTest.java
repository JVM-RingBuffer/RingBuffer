package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class PrefilledOneToOneBlockingBatchTest implements RingBufferTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runTest();
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
        final RingBuffer<Event> ringBuffer = PrefilledOneToOneBlockingTest.RING_BUFFER;
        AdvancingBatchReader reader = new AdvancingBatchReader(NUM_ITERATIONS, ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
