package bench.object;

import org.ringbuffer.object.RingBuffer;

class ManyReadersBlockingBatchContentionPerfBenchmark extends ManyReadersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return ManyReadersBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
