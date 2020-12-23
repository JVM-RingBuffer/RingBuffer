package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.ConcurrentStack;

public class ConcurrentStackContentionTest extends RingBufferTest {
    private static class Holder {
        static final ConcurrentStack<Event> stack = new ConcurrentStack<>(NOT_ONE_TO_ONE_SIZE * 2);

        static {
            for (int i = 0; i < stack.getCapacity() / 2; i++) {
                stack.push(new Event(0));
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentStackContentionTest test = new ConcurrentStackContentionTest();
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
        Writer.startGroupAsync(Holder.stack, profiler);
        return Reader.runGroupAsync(Holder.stack, profiler);
    }
}
