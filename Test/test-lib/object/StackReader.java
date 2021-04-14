package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.Stack;
import test.AbstractReader;
import test.TestThreadGroup;

class StackReader extends TestThread implements AbstractReader {
    static long runGroupAsync(Stack<Event> stack, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new StackReader(numIterations, stack));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    private long sum;

    private StackReader(int numIterations, Stack<Event> stack) {
        super(numIterations, stack);
    }

    @Override
    public long getSum() {
        return sum;
    }

    @Override
    protected void loop() {
        sum = collect();
    }

    long collect() {
        Stack<Event> stack = getStack();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += stack.take().getData();
        }
        return sum;
    }
}
