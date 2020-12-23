package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.OneToOneContentionTest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedConcurrentOneToOneContentionTest extends OneToOneContentionTest {
    static final Queue<Event> QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new LinkedConcurrentOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
