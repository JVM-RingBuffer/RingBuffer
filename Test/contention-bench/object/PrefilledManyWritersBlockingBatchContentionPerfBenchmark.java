package bench.object;

import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyWritersBlockingBatchContentionPerfBenchmark extends PrefilledManyWritersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchContentionPerfBenchmark().runBenchmark();
    }

    @Override
    PrefilledRingBuffer2<Event> getRingBuffer() {
        return PrefilledManyWritersBlockingContentionPerfBenchmark.RING_BUFFER;
    }
}
