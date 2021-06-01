package bench.competitors;

import bench.AbstractBenchmarkThread;
import bench.object.Event;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

abstract class BenchmarkThread extends AbstractBenchmarkThread {
    BenchmarkThread(int numIterations, Queue<Event> queue) {
        super(numIterations, queue);
    }

    @SuppressWarnings("unchecked")
    Queue<Event> getQueue() {
        return (Queue<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    BlockingQueue<Event> getBlockingQueue() {
        return (BlockingQueue<Event>) dataStructure;
    }
}
