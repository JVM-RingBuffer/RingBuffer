package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.ObjectRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

class Reader extends TestThread implements AbstractReader {
    static long runGroupAsync(ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Reader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        Reader reader = new Reader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    Reader(int numIterations, ObjectRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    public long getSum() {
        return sum;
    }

    @Override
    protected void loop() {
        sum = collect();
    }

    long collect() {
        ObjectRingBuffer<Event> ringBuffer = getObjectRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.take().getData();
        }
        return sum;
    }
}
