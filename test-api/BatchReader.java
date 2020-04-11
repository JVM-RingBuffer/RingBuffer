package test;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Int;

class BatchReader extends Reader {
    static long runGroupAsync(int readBufferSize, RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BatchReader(numIterations, readBufferSize, ringBuffer));
        group.start();
        group.reportPerformance();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        BatchReader reader = new BatchReader(numIterations, readBufferSize, ringBuffer);
        reader.start();
        reader.reportPerformance();
        return reader.getSum();
    }

    static long runSync(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        BatchReader reader = new BatchReader(numIterations, readBufferSize, ringBuffer);
        reader.run();
        reader.reportPerformance();
        return reader.getSum();
    }

    private final Event[] readBuffer;

    BatchReader(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        this(numIterations, new Event[readBufferSize], ringBuffer);
    }

    BatchReader(int numIterations, Event[] readBuffer, RingBuffer<Event> ringBuffer) {
        super(Int.ceilDiv(numIterations, readBuffer.length), ringBuffer);
        this.readBuffer = readBuffer;
    }

    Event[] getReadBuffer() {
        return readBuffer;
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Event[] buffer = readBuffer;
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.fill(buffer);
            for (Event event : buffer) {
                sum += event.getData();
            }
        }
        return sum;
    }
}
