package bench.competitors;

import bench.object.Event;
import bench.object.LockfreeManyToManyContentionBenchmark;
import eu.menzani.benchmark.Profiler;
import org.jctools.queues.MpmcArrayQueue;

import java.util.Queue;

public class JCToolsManyToManyContentionBenchmark extends LockfreeManyToManyContentionBenchmark {
    static final Queue<Event> QUEUE = new MpmcArrayQueue<>(LOCKFREE_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new JCToolsManyToManyContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
