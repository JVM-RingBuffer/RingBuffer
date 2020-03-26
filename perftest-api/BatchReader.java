package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Array;

class BatchReader extends Reader {
    static TestThreadGroup runGroupAsync(int readBufferSize, RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, readBufferSize, ringBuffer));
    }

    static BatchReader runAsync(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        BatchReader thread = new BatchReader(numIterations, readBufferSize, ringBuffer);
        thread.start();
        return thread;
    }

    static BatchReader runSync(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        BatchReader thread = new BatchReader(numIterations, readBufferSize, ringBuffer);
        thread.run();
        return thread;
    }

    private final Array<Event> readBuffer;

    BatchReader(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        this(numIterations, Array.allocate(readBufferSize), ringBuffer);
    }

    BatchReader(int numIterations, Array<Event> readBuffer, RingBuffer<Event> ringBuffer) {
        super(ceilDiv(numIterations, readBuffer.getCapacity()), ringBuffer);
        this.readBuffer = readBuffer;
    }

    private static int ceilDiv(int dividend, int divisor) {
        return (dividend - 1) / divisor + 1;
    }

    Array<Event> getReadBuffer() {
        return readBuffer;
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Array<Event> buffer = readBuffer;
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
