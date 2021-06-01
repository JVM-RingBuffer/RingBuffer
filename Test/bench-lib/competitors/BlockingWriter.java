package bench.competitors;

import bench.BenchmarkThreadGroup;
import bench.object.Event;
import eu.menzani.benchmark.Profiler;

import java.util.concurrent.BlockingQueue;

class BlockingWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(BlockingQueue<Event> queue, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new BlockingWriter(numIterations, queue));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(BlockingQueue<Event> queue, Profiler profiler) {
        startGroupAsync(queue, profiler).waitForCompletion(null);
    }

    static BlockingWriter startAsync(int numIterations, BlockingQueue<Event> queue, Profiler profiler) {
        BlockingWriter writer = new BlockingWriter(numIterations, queue);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, BlockingQueue<Event> queue, Profiler profiler) {
        startAsync(numIterations, queue, profiler).waitForCompletion(null);
    }

    private BlockingWriter(int numIterations, BlockingQueue<Event> queue) {
        super(numIterations, queue);
    }

    @Override
    protected void loop() {
        try {
            BlockingQueue<Event> queue = getBlockingQueue();
            for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
                queue.put(new Event(numIterations));
            }
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}
