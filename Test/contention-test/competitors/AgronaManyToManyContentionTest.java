package test.competitors;

import eu.menzani.benchmark.Profiler;
import org.agrona.concurrent.ManyToManyConcurrentArrayQueue;
import test.object.Event;
import test.object.LockfreeManyToManyContentionTest;

import java.util.Queue;

public class AgronaManyToManyContentionTest extends LockfreeManyToManyContentionTest {
    static final Queue<Event> QUEUE = new ManyToManyConcurrentArrayQueue<>(LOCKFREE_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new AgronaManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
