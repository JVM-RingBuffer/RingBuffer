package test.object;

import org.ringbuffer.object.RingBuffer;

class ManyReadersBlockingBatchContentionPerfTest extends ManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return ManyReadersBlockingContentionPerfTest.RING_BUFFER;
    }
}
