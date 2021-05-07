package test.object;

import org.ringbuffer.object.LockfreeRingBuffer;

import static test.object.ProducersToProcessorToConsumersContentionTest.CONSUMERS_RING_BUFFER;

class Processor extends TestThread {
    static Processor startAsync(int numIterations, LockfreeRingBuffer<Event> producersRingBuffer) {
        Processor processor = new Processor(numIterations, producersRingBuffer);
        processor.startNow(null);
        return processor;
    }

    static void runAsync(int numIterations, LockfreeRingBuffer<Event> producersRingBuffer) {
        startAsync(numIterations, producersRingBuffer).waitForCompletion(null);
    }

    private Processor(int numIterations, LockfreeRingBuffer<Event> producersRingBuffer) {
        super(numIterations, producersRingBuffer);
    }

    @Override
    protected void loop() {
        LockfreeRingBuffer<Event> producersRingBuffer = getLockfreeRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int eventData = producersRingBuffer.take().getData();
            int key = CONSUMERS_RING_BUFFER.nextKey();
            CONSUMERS_RING_BUFFER.next(key).setData(eventData);
            CONSUMERS_RING_BUFFER.put(key);
        }
    }
}
