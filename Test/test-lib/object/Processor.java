package test.object;

import org.ringbuffer.object.ObjectRingBuffer;
import org.ringbuffer.object.RingBuffer;

import static test.object.ProducersToProcessorToConsumersContentionTest.CONSUMERS_RING_BUFFER;

class Processor extends TestThread {
    static Processor startAsync(int numIterations, ObjectRingBuffer<Event> producersRingBuffer) {
        Processor processor = new Processor(numIterations, producersRingBuffer);
        processor.startNow(null);
        return processor;
    }

    static void runAsync(int numIterations, ObjectRingBuffer<Event> producersRingBuffer) {
        startAsync(numIterations, producersRingBuffer).waitForCompletion(null);
    }

    private Processor(int numIterations, ObjectRingBuffer<Event> producersRingBuffer) {
        super(numIterations, producersRingBuffer);
    }

    @Override
    protected void loop() {
        RingBuffer<Event> producersRingBuffer = getRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int eventData = producersRingBuffer.take().getData();
            int key = CONSUMERS_RING_BUFFER.nextKey();
            CONSUMERS_RING_BUFFER.next(key).setData(eventData);
            CONSUMERS_RING_BUFFER.put(key);
        }
    }
}
