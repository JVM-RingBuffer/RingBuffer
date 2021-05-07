package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreeRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

class LockfreeReader extends TestThread implements AbstractReader {
    static long runGroupAsync(LockfreeRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new LockfreeReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, LockfreeRingBuffer<Event> ringBuffer, Profiler profiler) {
        LockfreeReader reader = new LockfreeReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    LockfreeReader(int numIterations, LockfreeRingBuffer<Event> ringBuffer) {
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
        LockfreeRingBuffer<Event> ringBuffer = getLockfreeRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.take().getData();
        }
        return sum;
    }
}
