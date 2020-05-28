package test.marshalling;

import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.AbstractReader;
import test.Profiler;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.*;

class DirectReader extends TestThread implements AbstractReader {
    static long runGroupAsync(DirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        DirectReader reader = new DirectReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    DirectReader(int numIterations, DirectMarshallingRingBuffer ringBuffer) {
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
        DirectMarshallingRingBuffer ringBuffer = getDirectMarshallingRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
            ringBuffer.advance();
        }
        return sum;
    }
}
