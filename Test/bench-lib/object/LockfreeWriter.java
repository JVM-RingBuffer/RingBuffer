package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreeRingBuffer;

class LockfreeWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(LockfreeRingBuffer<Event> ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new LockfreeWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(LockfreeRingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static LockfreeWriter startAsync(int numIterations, LockfreeRingBuffer<Event> ringBuffer, Profiler profiler) {
        LockfreeWriter writer = new LockfreeWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, LockfreeRingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private LockfreeWriter(int numIterations, LockfreeRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        LockfreeRingBuffer<Event> ringBuffer = getLockfreeRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            ringBuffer.put(new Event(numIterations));
        }
    }
}
