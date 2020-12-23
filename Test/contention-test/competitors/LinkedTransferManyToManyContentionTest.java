package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyToManyContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferManyToManyContentionTest extends ManyToManyContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedTransferQueue<>();

    public static void main(String[] args) {
        new LinkedTransferManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
