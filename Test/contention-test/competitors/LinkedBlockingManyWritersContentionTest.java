package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyWritersContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingManyWritersContentionTest extends ManyWritersContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new LinkedBlockingManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
