package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreePrefilledRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

class LockfreePrefilledReader extends TestThread implements AbstractReader {
    static long runGroupAsync(LockfreePrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new LockfreePrefilledReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, LockfreePrefilledRingBuffer<Event> ringBuffer, Profiler profiler) {
        LockfreePrefilledReader reader = new LockfreePrefilledReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    LockfreePrefilledReader(int numIterations, LockfreePrefilledRingBuffer<Event> ringBuffer) {
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
        LockfreePrefilledRingBuffer<Event> ringBuffer = getLockfreePrefilledRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.take().getData();
        }
        return sum;
    }
}
