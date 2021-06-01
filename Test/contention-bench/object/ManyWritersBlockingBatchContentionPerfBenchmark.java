package bench.object;

import org.ringbuffer.object.RingBuffer;

public class ManyWritersBlockingBatchContentionPerfBenchmark extends ManyWritersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return ManyWritersBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
