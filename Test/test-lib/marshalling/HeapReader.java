package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.HeapOffsets.INT;

class HeapReader extends TestThread implements AbstractReader {
    static long runGroupAsync(HeapRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new HeapReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, HeapRingBuffer ringBuffer, Profiler profiler) {
        HeapReader reader = new HeapReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    HeapReader(int numIterations, Object ringBuffer) {
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
        HeapRingBuffer ringBuffer = getHeapRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.take(INT);
            sum += ringBuffer.readInt(offset);
            ringBuffer.advance(offset + INT);
        }
        return sum;
    }
}
