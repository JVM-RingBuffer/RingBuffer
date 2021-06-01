package bench.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyReadersBlockingBatchContentionPerfBenchmark extends PrefilledManyReadersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledManyReadersBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
