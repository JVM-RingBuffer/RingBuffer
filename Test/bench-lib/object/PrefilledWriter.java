package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

class PrefilledWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new PrefilledWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static PrefilledWriter startAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        PrefilledWriter writer = new PrefilledWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private PrefilledWriter(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledRingBuffer<Event> ringBuffer = getPrefilledRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            ringBuffer.next(key).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}
