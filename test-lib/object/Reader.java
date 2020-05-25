package test.object;

import eu.menzani.ringbuffer.object.RingBuffer;
import test.AbstractReader;
import test.TestThreadGroup;

class Reader extends TestThread implements AbstractReader {
    static TestThreadGroup startGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Reader(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static long runGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = startGroupAsync(ringBuffer);
        group.waitForCompletion();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader reader = new Reader(numIterations, ringBuffer);
        reader.startNow();
        reader.waitForCompletion();
        return reader.getSum();
    }

    private long sum;

    Reader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "Reader";
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
        RingBuffer<Event> ringBuffer = getRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.take().getData();
            ringBuffer.advance();
        }
        return sum;
    }
}
