package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.INT;

class DirectClearingWriter extends TestThread {
    private static TestThreadGroup startGroupAsync(DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectClearingWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static DirectClearingWriter startAsync(int numIterations, DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        DirectClearingWriter writer = new DirectClearingWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private DirectClearingWriter(int numIterations, DirectClearingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectClearingRingBuffer ringBuffer = getDirectClearingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next();
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
