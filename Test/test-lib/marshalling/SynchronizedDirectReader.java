package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.INT;

class SynchronizedDirectReader extends HeapReader {
    static long runGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedDirectReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedDirectReader reader = new SynchronizedDirectReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private SynchronizedDirectReader(int numIterations, DirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        DirectRingBuffer ringBuffer = getDirectRingBuffer();
        Object readMonitor = ringBuffer.getReadMonitor();
        int data;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (readMonitor) {
                long offset = ringBuffer.take(INT);
                data = ringBuffer.readInt(offset);
                ringBuffer.advance(offset + INT);
            }
            sum += data;
        }
        return sum;
    }
}
