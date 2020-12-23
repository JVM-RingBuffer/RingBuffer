package test.competitors;

import eu.menzani.benchmark.Profiler;
import test.object.Event;
import test.object.OneToOneContentionTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferOneToOneContentionTest extends OneToOneContentionTest {
    static final BlockingQueue<Event> QUEUE = new LinkedTransferQueue<>();

    public static void main(String[] args) {
        new LinkedTransferOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.startAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
