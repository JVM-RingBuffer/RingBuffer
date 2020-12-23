package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyToManyContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingManyToManyContentionTest extends ManyToManyContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>(NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new LinkedBlockingManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
