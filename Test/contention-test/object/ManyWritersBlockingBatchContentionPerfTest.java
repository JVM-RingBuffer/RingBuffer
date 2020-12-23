package test.object;

import org.ringbuffer.object.RingBuffer;

public class ManyWritersBlockingBatchContentionPerfTest extends ManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return ManyWritersBlockingContentionPerfTest.RING_BUFFER;
    }
}
