package test.competitors;

import eu.menzani.benchmark.Profiler;
import org.jctools.queues.SpmcArrayQueue;
import test.object.Event;
import test.object.LockfreeManyReadersContentionTest;

import java.util.Queue;

public class JCToolsManyReadersContentionTest extends LockfreeManyReadersContentionTest {
    static final Queue<Event> QUEUE = new SpmcArrayQueue<>(LOCKFREE_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new JCToolsManyReadersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
