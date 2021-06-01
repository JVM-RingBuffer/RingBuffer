package bench.competitors;

import bench.object.Event;
import bench.object.LockfreeManyReadersContentionBenchmark;
import eu.menzani.benchmark.Profiler;
import org.jctools.queues.SpmcArrayQueue;

import java.util.Queue;

public class JCToolsManyReadersContentionBenchmark extends LockfreeManyReadersContentionBenchmark {
    static final Queue<Event> QUEUE = new SpmcArrayQueue<>(LOCKFREE_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new JCToolsManyReadersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
