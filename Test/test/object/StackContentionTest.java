package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.Stack;

public class StackContentionTest extends RingBufferTest {
    private static class Holder {
        static final Stack<Event> stack = new Stack<>(NOT_ONE_TO_ONE_SIZE * 2);

        static {
            stack.pushMany(stack.getCapacity() / 2, FILLER);
        }
    }

    public static void main(String[] args) {
        StackContentionTest test = new StackContentionTest();
        test.doNotCheckSum();
        test.runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        StackWriter.startGroupAsync(Holder.stack, profiler);
        return StackReader.runGroupAsync(Holder.stack, profiler);
    }
}
