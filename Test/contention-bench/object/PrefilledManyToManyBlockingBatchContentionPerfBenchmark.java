package bench.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

class PrefilledManyToManyBlockingBatchContentionPerfBenchmark extends PrefilledManyToManyBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledManyToManyBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
