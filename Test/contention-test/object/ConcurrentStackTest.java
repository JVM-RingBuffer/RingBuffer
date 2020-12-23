package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.ConcurrentStack;

public class ConcurrentStackTest extends ConcurrentStackContentionTest {
    private static final ConcurrentStack<Event> STACK = new ConcurrentStack<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new ConcurrentStackTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(STACK, profiler);
        return Reader.runGroupAsync(STACK, profiler);
    }
}
