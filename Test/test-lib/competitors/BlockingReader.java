package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.TestThreadGroup;
import test.object.Event;

import java.util.concurrent.BlockingQueue;

class BlockingReader extends Reader {
    static long runGroupAsync(BlockingQueue<Event> queue, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BlockingReader(numIterations, queue));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, BlockingQueue<Event> queue, Profiler profiler) {
        BlockingReader reader = new BlockingReader(numIterations, queue);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private BlockingReader(int numIterations, BlockingQueue<Event> queue) {
        super(numIterations, queue);
    }

    @Override
    long collect() {
        try {
            BlockingQueue<Event> queue = getBlockingQueue();
            long sum = 0L;
            for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
                sum += queue.take().getData();
            }
            return sum;
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}
