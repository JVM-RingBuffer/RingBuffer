package bench.competitors;

import bench.object.Event;
import bench.object.LockfreeManyWritersContentionBenchmark;
import eu.menzani.benchmark.Profiler;
import org.agrona.concurrent.ManyToOneConcurrentArrayQueue;

import java.util.Queue;

public class AgronaManyWritersContentionBenchmark extends LockfreeManyWritersContentionBenchmark {
    static final Queue<Event> QUEUE = new ManyToOneConcurrentArrayQueue<>(LOCKFREE_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new AgronaManyWritersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
