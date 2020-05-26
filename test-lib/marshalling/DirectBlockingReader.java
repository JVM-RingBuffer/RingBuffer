package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.DirectOffsets.*;

class DirectBlockingReader extends TestThread implements AbstractReader {
    static long runGroupAsync(DirectMarshallingBlockingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectBlockingReader(numIterations, ringBuffer));
        group.start();
        group.waitForCompletion();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer) {
        DirectBlockingReader reader = new DirectBlockingReader(numIterations, ringBuffer);
        reader.startNow();
        reader.waitForCompletion();
        return reader.getSum();
    }

    private long sum;

    DirectBlockingReader(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer) {
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
        DirectMarshallingBlockingRingBuffer ringBuffer = getDirectMarshallingBlockingRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.take(INT);
            sum += ringBuffer.readInt(offset);
            ringBuffer.advance(offset + INT);
        }
        return sum;
    }
}
