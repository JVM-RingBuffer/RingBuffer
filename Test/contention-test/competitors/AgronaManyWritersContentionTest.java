package test.competitors;

import eu.menzani.benchmark.Profiler;
import org.agrona.concurrent.ManyToOneConcurrentArrayQueue;
import test.object.Event;
import test.object.FastManyWritersContentionTest;

import java.util.Queue;

public class AgronaManyWritersContentionTest extends FastManyWritersContentionTest {
    static final Queue<Event> QUEUE = new ManyToOneConcurrentArrayQueue<>(FAST_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new AgronaManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
