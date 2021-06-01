package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer2;

class PrefilledWriter2 extends BenchmarkThread {
    private static BenchmarkThreadGroup startGroupAsync(PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new PrefilledWriter2(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static PrefilledWriter2 startAsync(int numIterations, PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        PrefilledWriter2 writer = new PrefilledWriter2(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private PrefilledWriter2(int numIterations, PrefilledRingBuffer2<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledRingBuffer2<Event> ringBuffer = getPrefilledRingBuffer2();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            int putKey = ringBuffer.nextPutKey(key);
            ringBuffer.next(key, putKey).setData(numIterations);
            ringBuffer.put(putKey);
        }
    }
}
