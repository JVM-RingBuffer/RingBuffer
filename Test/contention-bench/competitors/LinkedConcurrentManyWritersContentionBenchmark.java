package bench.competitors;

import bench.object.Event;
import bench.object.ManyWritersContentionBenchmark;
import eu.menzani.benchmark.Profiler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedConcurrentManyWritersContentionBenchmark extends ManyWritersContentionBenchmark {
    static final Queue<Event> QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new LinkedConcurrentManyWritersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
