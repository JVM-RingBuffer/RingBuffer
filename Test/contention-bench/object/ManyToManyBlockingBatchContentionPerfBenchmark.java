package bench.object;

import org.ringbuffer.object.RingBuffer;

class ManyToManyBlockingBatchContentionPerfBenchmark extends ManyToManyBlockingContentionBenchmark {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    RingBuffer<Event> getRingBuffer() {
        return ManyToManyBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
