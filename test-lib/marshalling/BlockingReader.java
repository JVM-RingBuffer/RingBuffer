package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.Offsets.*;

class BlockingReader extends TestThread implements AbstractReader {
    static TestThreadGroup startGroupAsync(MarshallingBlockingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BlockingReader(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static long runGroupAsync(MarshallingBlockingRingBuffer ringBuffer) {
        TestThreadGroup group = startGroupAsync(ringBuffer);
        group.waitForCompletion();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, MarshallingBlockingRingBuffer ringBuffer) {
        BlockingReader reader = new BlockingReader(numIterations, ringBuffer);
        reader.startNow();
        reader.waitForCompletion();
        return reader.getSum();
    }

    private long sum;

    BlockingReader(int numIterations, MarshallingBlockingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "BlockingReader";
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
