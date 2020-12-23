package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyToManyContentionTest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedConcurrentManyToManyContentionTest extends ManyToManyContentionTest {
    static final Queue<Event> QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new LinkedConcurrentManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
