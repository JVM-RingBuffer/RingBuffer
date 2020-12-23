package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.TestThreadGroup;
import test.object.Event;

import java.util.Queue;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(Queue<Event> queue, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, queue));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(Queue<Event> queue, Profiler profiler) {
        startGroupAsync(queue, profiler).waitForCompletion(null);
    }

    static Writer startAsync(int numIterations, Queue<Event> queue, Profiler profiler) {
        Writer writer = new Writer(numIterations, queue);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, Queue<Event> queue, Profiler profiler) {
        startAsync(numIterations, queue, profiler).waitForCompletion(null);
    }

    private Writer(int numIterations, Queue<Event> queue) {
        super(numIterations, queue);
    }

    @Override
    protected void loop() {
        Queue<Event> queue = getQueue();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            queue.offer(new Event(numIterations));
        }
    }
}
