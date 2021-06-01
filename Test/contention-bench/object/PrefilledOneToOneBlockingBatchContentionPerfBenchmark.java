package bench.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledOneToOneBlockingBatchContentionPerfBenchmark extends PrefilledOneToOneBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledOneToOneBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
