package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

import static eu.menzani.struct.HeapOffsets.INT;

class SynchronizedHeapWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(HeapRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new SynchronizedHeapWriter(numIterations, ringBuffer));
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
