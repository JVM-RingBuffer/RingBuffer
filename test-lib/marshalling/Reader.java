package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;
import test.TestThread;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.Offsets.*;

class Reader extends TestThread {
    static TestThreadGroup startGroupAsync(MarshallingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Reader(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static long runGroupAsync(MarshallingRingBuffer ringBuffer) {
        TestThreadGroup group = startGroupAsync(ringBuffer);
        group.waitForCompletion();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, MarshallingRingBuffer ringBuffer) {
        Reader reader = new Reader(numIterations, ringBuffer);
        reader.startNow();
        reader.waitForCompletion();
        return reader.getSum();
    }

    private long sum;

    Reader(int numIterations, MarshallingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "Reader";
    }

    long getSum() {
        return sum;
    }

    @Override
    protected void loop() {
        sum = collect();
    }

    long collect() {
        MarshallingRingBuffer ringBuffer = getHeapRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
            ringBuffer.advance();
        }
        return sum;
    }
}
