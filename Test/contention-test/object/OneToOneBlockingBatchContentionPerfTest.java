package test.object;

import org.ringbuffer.object.RingBuffer;

public class OneToOneBlockingBatchContentionPerfTest extends OneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchContentionPerfTest().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return OneToOneBlockingContentionPerfTest.RING_BUFFER;
    }
}
