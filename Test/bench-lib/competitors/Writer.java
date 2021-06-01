package bench.competitors;

import bench.BenchmarkThreadGroup;
import bench.object.Event;
import eu.menzani.benchmark.Profiler;

import java.util.Queue;

class Writer extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(Queue<Event> queue, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new Writer(numIterations, queue));
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
