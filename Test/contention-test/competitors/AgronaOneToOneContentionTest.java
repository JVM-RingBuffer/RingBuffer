package test.competitors;

import eu.menzani.benchmark.Profiler;
import org.agrona.concurrent.OneToOneConcurrentArrayQueue;
import test.object.Event;
import test.object.LockfreeOneToOneContentionTest;

import java.util.Queue;

public class AgronaOneToOneContentionTest extends LockfreeOneToOneContentionTest {
    static final Queue<Event> QUEUE = new OneToOneConcurrentArrayQueue<>(LOCKFREE_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new AgronaOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
