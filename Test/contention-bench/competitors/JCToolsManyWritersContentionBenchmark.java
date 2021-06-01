package bench.competitors;

import bench.object.Event;
import bench.object.LockfreeManyWritersContentionBenchmark;
import eu.menzani.benchmark.Profiler;
import org.jctools.queues.MpscArrayQueue;

import java.util.Queue;

public class JCToolsManyWritersContentionBenchmark extends LockfreeManyWritersContentionBenchmark {
    static final Queue<Event> QUEUE = new MpscArrayQueue<>(LOCKFREE_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new JCToolsManyWritersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
