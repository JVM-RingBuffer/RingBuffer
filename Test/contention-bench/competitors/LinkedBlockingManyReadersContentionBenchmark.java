package bench.competitors;

import bench.object.Event;
import bench.object.ManyReadersContentionBenchmark;
import eu.menzani.benchmark.Profiler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingManyReadersContentionBenchmark extends ManyReadersContentionBenchmark {
    static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new LinkedBlockingManyReadersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
