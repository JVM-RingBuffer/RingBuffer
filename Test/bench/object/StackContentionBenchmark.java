package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.Stack;

public class StackContentionBenchmark extends RingBufferBenchmark {
    private static class Holder {
        static final Stack<Event> stack = new Stack<>(NOT_ONE_TO_ONE_SIZE * 2);

        static {
            stack.pushMany(stack.getCapacity() / 2, FILLER);
        }
    }

    public static void main(String[] args) {
        StackContentionBenchmark benchmark = new StackContentionBenchmark();
        benchmark.doNotCheckSum();
        benchmark.runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        StackWriter.startGroupAsync(Holder.stack, profiler);
        return StackReader.runGroupAsync(Holder.stack, profiler);
    }
}
