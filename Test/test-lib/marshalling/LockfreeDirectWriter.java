package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.INT;

class LockfreeDirectWriter extends TestThread {
    static TestThreadGroup startGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new LockfreeDirectWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static LockfreeDirectWriter startAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        LockfreeDirectWriter writer = new LockfreeDirectWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private LockfreeDirectWriter(int numIterations, DirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectRingBuffer ringBuffer = getDirectRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset);
        }
    }
}
