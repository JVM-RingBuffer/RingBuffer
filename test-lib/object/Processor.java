package test.object;

import org.ringbuffer.object.EmptyRingBuffer;
import org.ringbuffer.object.RingBuffer;

import static test.object.ProducersToProcessorToConsumersContentionTest.*;

class Processor extends TestThread {
    static Processor startAsync(int numIterations, RingBuffer<Event> producersRingBuffer) {
        Processor processor = new Processor(numIterations, producersRingBuffer);
        processor.startNow(null);
        return processor;
    }

    static void runAsync(int numIterations, RingBuffer<Event> producersRingBuffer) {
        startAsync(numIterations, producersRingBuffer).waitForCompletion(null);
    }

    private Processor(int numIterations, RingBuffer<Event> producersRingBuffer) {
        super(numIterations, producersRingBuffer);
    }

    @Override
    protected void loop() {
        EmptyRingBuffer<Event> producersRingBuffer = getEmptyRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int eventData = producersRingBuffer.take().getData();
            int key = CONSUMERS_RING_BUFFER.nextKey();
            CONSUMERS_RING_BUFFER.next(key).setData(eventData);
            CONSUMERS_RING_BUFFER.put(key);
        }
    }
}