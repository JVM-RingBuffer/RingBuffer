package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.DirectOffsets.*;

class DirectReader extends TestThread implements AbstractReader {
    static long runGroupAsync(DirectMarshallingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectReader(numIterations, ringBuffer));
        group.start();
        group.waitForCompletion();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectMarshallingRingBuffer ringBuffer) {
        DirectReader reader = new DirectReader(numIterations, ringBuffer);
        reader.startNow();
        reader.waitForCompletion();
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
