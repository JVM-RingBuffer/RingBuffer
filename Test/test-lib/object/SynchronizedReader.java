package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.ObjectRingBuffer;
import test.TestThreadGroup;

class SynchronizedReader extends Reader {
    static long runGroupAsync(ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        SynchronizedReader reader = new SynchronizedReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private SynchronizedReader(int numIterations, ObjectRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        ObjectRingBuffer<Event> ringBuffer = getObjectRingBuffer();
        Object readMonitor = ringBuffer.getReadMonitor();
        int data;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (readMonitor) {
                data = ringBuffer.take().getData();
            }
            sum += data;
        }
        return sum;
    }
}
