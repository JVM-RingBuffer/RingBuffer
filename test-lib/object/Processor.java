package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;
import eu.menzani.ringbuffer.object.RingBuffer;

import static test.object.ProducersToProcessorToConsumersTest.*;

class Processor extends TestThread {
    static Processor startAsync(int numIterations, RingBuffer<Event> producersRingBuffer) {
        Processor processor = new Processor(numIterations, producersRingBuffer);
        processor.startNow();
        return processor;
    }

    static void runAsync(int numIterations, RingBuffer<Event> producersRingBuffer) {
        startAsync(numIterations, producersRingBuffer).waitForCompletion();
    }

    private Processor(int numIterations, RingBuffer<Event> producersRingBuffer) {
        super(numIterations, producersRingBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "Processor";
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
