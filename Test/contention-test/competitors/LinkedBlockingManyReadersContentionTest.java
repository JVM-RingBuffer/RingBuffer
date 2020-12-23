package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyReadersContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingManyReadersContentionTest extends ManyReadersContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new LinkedBlockingManyReadersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
