package bench.competitors;

import bench.object.Event;
import bench.object.LockfreeOneToOneContentionBenchmark;
import eu.menzani.benchmark.Profiler;
import org.agrona.concurrent.OneToOneConcurrentArrayQueue;

import java.util.Queue;

public class AgronaOneToOneContentionBenchmark extends LockfreeOneToOneContentionBenchmark {
    static final Queue<Event> QUEUE = new OneToOneConcurrentArrayQueue<>(LOCKFREE_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new AgronaOneToOneContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
