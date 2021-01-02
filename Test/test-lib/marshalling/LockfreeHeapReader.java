package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.HeapOffsets.INT;

class LockfreeHeapReader extends HeapReader {
    static long runGroupAsync(HeapRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new LockfreeHeapReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, HeapRingBuffer ringBuffer, Profiler profiler) {
        LockfreeHeapReader reader = new LockfreeHeapReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private LockfreeHeapReader(int numIterations, HeapRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        HeapRingBuffer ringBuffer = getHeapRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
        }
        return sum;
    }
}
