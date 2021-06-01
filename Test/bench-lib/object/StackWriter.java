package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.Stack;

class StackWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(Stack<Event> stack, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new StackWriter(numIterations, stack));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(Stack<Event> stack, Profiler profiler) {
        startGroupAsync(stack, profiler).waitForCompletion(null);
    }

    private StackWriter(int numIterations, Stack<Event> stack) {
        super(numIterations, stack);
    }

    @Override
    protected void loop() {
        Stack<Event> stack = getStack();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            stack.put(new Event(numIterations));
        }
    }
}
