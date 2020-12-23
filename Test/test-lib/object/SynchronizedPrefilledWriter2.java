package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer2;
import test.TestThreadGroup;

class SynchronizedPrefilledWriter2 extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedPrefilledWriter2(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    private static SynchronizedPrefilledWriter2 startAsync(int numIterations, PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        SynchronizedPrefilledWriter2 writer = new SynchronizedPrefilledWriter2(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private SynchronizedPrefilledWriter2(int numIterations, PrefilledRingBuffer2<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledRingBuffer2<Event> ringBuffer = getPrefilledRingBuffer2();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (ringBuffer) {
                int key = ringBuffer.nextKey();
                int putKey = ringBuffer.nextPutKey(key);
                ringBuffer.next(key, putKey).setData(numIterations);
                ringBuffer.put(putKey);
            }
        }
    }
}
