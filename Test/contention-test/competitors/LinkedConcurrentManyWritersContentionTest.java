package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyWritersContentionTest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedConcurrentManyWritersContentionTest extends ManyWritersContentionTest {
    static final Queue<Event> QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new LinkedConcurrentManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
