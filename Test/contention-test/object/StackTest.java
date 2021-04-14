package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.Stack;

public class StackTest extends StackContentionTest {
    private static final Stack<Event> stack = new Stack<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new StackTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        StackWriter.runGroupAsync(stack, profiler);
        return StackReader.runGroupAsync(stack, profiler);
    }
}
