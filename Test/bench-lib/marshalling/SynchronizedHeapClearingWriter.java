package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;

import static eu.menzani.struct.HeapOffsets.INT;

class SynchronizedHeapClearingWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(HeapClearingRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new SynchronizedHeapClearingWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(HeapClearingRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    private static SynchronizedHeapClearingWriter startAsync(int numIterations, HeapClearingRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedHeapClearingWriter writer = new SynchronizedHeapClearingWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, HeapClearingRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private SynchronizedHeapClearingWriter(int numIterations, HeapClearingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        HeapClearingRingBuffer ringBuffer = getHeapClearingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (ringBuffer) {
                int offset = ringBuffer.next();
                ringBuffer.writeInt(offset, numIterations);
                ringBuffer.put(offset + INT);
            }
        }
    }
}
