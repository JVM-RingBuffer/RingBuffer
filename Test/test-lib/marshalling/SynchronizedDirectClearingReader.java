package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import test.TestThreadGroup;

import static eu.menzani.struct.DirectOffsets.INT;

class SynchronizedDirectClearingReader extends HeapReader {
    static long runGroupAsync(DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedDirectClearingReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedDirectClearingReader reader = new SynchronizedDirectClearingReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private SynchronizedDirectClearingReader(int numIterations, DirectClearingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        DirectClearingRingBuffer ringBuffer = getDirectClearingRingBuffer();
        Object readMonitor = ringBuffer.getReadMonitor();
        int data;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (readMonitor) {
                data = ringBuffer.readInt(ringBuffer.take(INT));
            }
            sum += data;
        }
        return sum;
    }
}
