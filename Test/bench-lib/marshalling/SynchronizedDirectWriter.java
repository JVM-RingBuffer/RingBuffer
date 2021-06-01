package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

import static eu.menzani.struct.DirectOffsets.INT;

class SynchronizedDirectWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new SynchronizedDirectWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    private static SynchronizedDirectWriter startAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedDirectWriter writer = new SynchronizedDirectWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private SynchronizedDirectWriter(int numIterations, DirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectRingBuffer ringBuffer = getDirectRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (ringBuffer) {
                long offset = ringBuffer.next(INT);
                ringBuffer.writeInt(offset, numIterations);
                ringBuffer.put(offset + INT);
            }
        }
    }
}
