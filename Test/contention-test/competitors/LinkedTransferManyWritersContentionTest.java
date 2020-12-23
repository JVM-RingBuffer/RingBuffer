package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.ManyWritersContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferManyWritersContentionTest extends ManyWritersContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedTransferQueue<>();

    public static void main(String[] args) {
        new LinkedTransferManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.startGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
