package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.LockfreeDirectRingBuffer;

import static eu.menzani.struct.DirectOffsets.INT;

class LockfreeDirectWriter extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(LockfreeDirectRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new LockfreeDirectWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(LockfreeDirectRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static LockfreeDirectWriter startAsync(int numIterations, LockfreeDirectRingBuffer ringBuffer, Profiler profiler) {
        LockfreeDirectWriter writer = new LockfreeDirectWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, LockfreeDirectRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private LockfreeDirectWriter(int numIterations, LockfreeDirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        LockfreeDirectRingBuffer ringBuffer = getLockfreeDirectRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset);
        }
    }
}
