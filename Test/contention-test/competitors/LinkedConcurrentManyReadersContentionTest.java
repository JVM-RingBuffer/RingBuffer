package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyReadersContentionTest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedConcurrentManyReadersContentionTest extends ManyReadersContentionTest {
    static final Queue<Event> QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new LinkedConcurrentManyReadersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
