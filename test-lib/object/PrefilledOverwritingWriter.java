package test.object;

import org.ringbuffer.object.PrefilledOverwritingRingBuffer;
import test.Profiler;
import test.TestThreadGroup;

class PrefilledOverwritingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledOverwritingWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static PrefilledOverwritingWriter startAsync(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        PrefilledOverwritingWriter writer = new PrefilledOverwritingWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private PrefilledOverwritingWriter(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledOverwritingRingBuffer<Event> ringBuffer = getPrefilledOverwritingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            ringBuffer.next(key).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}