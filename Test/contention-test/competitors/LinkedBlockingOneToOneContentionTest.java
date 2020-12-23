package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.OneToOneContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingOneToOneContentionTest extends OneToOneContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>(ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new LinkedBlockingOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
