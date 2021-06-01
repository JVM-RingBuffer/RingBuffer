package bench.competitors;

import bench.object.Event;
import bench.object.ManyWritersContentionBenchmark;
import eu.menzani.benchmark.Profiler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingManyWritersContentionBenchmark extends ManyWritersContentionBenchmark {
    static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new LinkedBlockingManyWritersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
