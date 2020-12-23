package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyWritersContentionTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayManyWritersContentionTest extends ManyWritersContentionTest {
    static final BlockingQueue<Event> QUEUE = new ArrayBlockingQueue<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new ArrayManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
