package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.Stack;

public class StackBenchmark extends StackContentionBenchmark {
    private static final Stack<Event> stack = new Stack<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new StackBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        StackWriter.runGroupAsync(stack, profiler);
        return StackReader.runGroupAsync(stack, profiler);
    }
}
