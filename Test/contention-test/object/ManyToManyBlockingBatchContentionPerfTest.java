package test.object;

import org.ringbuffer.object.RingBuffer;

class ManyToManyBlockingBatchContentionPerfTest extends ManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return ManyToManyBlockingContentionPerfTest.RING_BUFFER;
    }
}
