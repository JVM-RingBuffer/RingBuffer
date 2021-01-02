package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.INT;

class LockfreeDirectReader extends HeapReader {
    static long runGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new LockfreeDirectReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        LockfreeDirectReader reader = new LockfreeDirectReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private LockfreeDirectReader(int numIterations, DirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        DirectRingBuffer ringBuffer = getDirectRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
        }
        return sum;
    }
}
