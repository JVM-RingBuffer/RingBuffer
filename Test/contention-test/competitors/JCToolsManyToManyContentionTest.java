package test.competitors;

import eu.menzani.benchmark.Profiler;
import org.jctools.queues.MpmcArrayQueue;
import test.object.Event;
import test.object.FastManyToManyContentionTest;

import java.util.Queue;

public class JCToolsManyToManyContentionTest extends FastManyToManyContentionTest {
    static final Queue<Event> QUEUE = new MpmcArrayQueue<>(FAST_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new JCToolsManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
