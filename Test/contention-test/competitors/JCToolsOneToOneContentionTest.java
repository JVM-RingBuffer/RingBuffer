package test.competitors;

import eu.menzani.benchmark.Profiler;
import org.jctools.queues.SpscArrayQueue;
import test.object.Event;
import test.object.LockfreeOneToOneContentionTest;

import java.util.Queue;

public class JCToolsOneToOneContentionTest extends LockfreeOneToOneContentionTest {
    static final Queue<Event> QUEUE = new SpscArrayQueue<>(LOCKFREE_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new JCToolsOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
