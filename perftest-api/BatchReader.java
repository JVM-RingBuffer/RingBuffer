package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Int;

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
