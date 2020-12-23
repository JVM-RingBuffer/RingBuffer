package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;
import test.TestThreadGroup;

class SynchronizedPrefilledWriter extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedPrefilledWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    private static SynchronizedPrefilledWriter startAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        SynchronizedPrefilledWriter writer = new SynchronizedPrefilledWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private SynchronizedPrefilledWriter(int numIterations, PrefilledRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledRingBuffer<Event> ringBuffer = getPrefilledRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (ringBuffer) {
                int key = ringBuffer.nextKey();
                ringBuffer.next(key).setData(numIterations);
                ringBuffer.put(key);
            }
        }
    }
}
