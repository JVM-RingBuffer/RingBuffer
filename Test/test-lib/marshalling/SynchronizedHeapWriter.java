package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.HeapOffsets.INT;

class SynchronizedHeapWriter extends TestThread {
    static TestThreadGroup startGroupAsync(HeapRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedHeapWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(HeapRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    private static SynchronizedHeapWriter startAsync(int numIterations, HeapRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedHeapWriter writer = new SynchronizedHeapWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, HeapRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private SynchronizedHeapWriter(int numIterations, HeapRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        HeapRingBuffer ringBuffer = getHeapRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (ringBuffer) {
                int offset = ringBuffer.next(INT);
                ringBuffer.writeInt(offset, numIterations);
                ringBuffer.put(offset + INT);
            }
        }
    }
}
