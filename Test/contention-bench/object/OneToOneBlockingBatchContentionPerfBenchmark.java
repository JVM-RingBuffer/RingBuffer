package bench.object;

import org.ringbuffer.object.RingBuffer;

public class OneToOneBlockingBatchContentionPerfBenchmark extends OneToOneBlockingContentionBenchmark {
    public static void main(String[] args) {
        new OneToOneBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return OneToOneBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
