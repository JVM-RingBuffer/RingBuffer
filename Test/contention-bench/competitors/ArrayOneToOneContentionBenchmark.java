package bench.competitors;

import bench.object.Event;
import bench.object.OneToOneContentionBenchmark;
import eu.menzani.benchmark.Profiler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayOneToOneContentionBenchmark extends OneToOneContentionBenchmark {
    static final BlockingQueue<Event> QUEUE = new ArrayBlockingQueue<>(ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new ArrayOneToOneContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
