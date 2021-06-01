package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.LockfreeHeapRingBuffer;

import static eu.menzani.struct.HeapOffsets.INT;

class LockfreeHeapWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(LockfreeHeapRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new LockfreeHeapWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(LockfreeHeapRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static LockfreeHeapWriter startAsync(int numIterations, LockfreeHeapRingBuffer ringBuffer, Profiler profiler) {
        LockfreeHeapWriter writer = new LockfreeHeapWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, LockfreeHeapRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private LockfreeHeapWriter(int numIterations, LockfreeHeapRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        LockfreeHeapRingBuffer ringBuffer = getLockfreeHeapRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset);
        }
    }
}
