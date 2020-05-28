package test.marshalling;

import org.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import test.AbstractReader;
import test.Profiler;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.Offsets.*;

class BlockingReader extends TestThread implements AbstractReader {
    static long runGroupAsync(MarshallingBlockingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BlockingReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, MarshallingBlockingRingBuffer ringBuffer, Profiler profiler) {
        BlockingReader reader = new BlockingReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    BlockingReader(int numIterations, MarshallingBlockingRingBuffer ringBuffer) {
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
        MarshallingBlockingRingBuffer ringBuffer = getMarshallingBlockingRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.take(INT);
            sum += ringBuffer.readInt(offset);
            ringBuffer.advance(offset + INT);
        }
        return sum;
    }
}
