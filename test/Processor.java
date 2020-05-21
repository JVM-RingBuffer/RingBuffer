package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;

import static test.ProducersToProcessorToConsumersTest.*;

class Processor extends TestThread {
    static Processor startAsync(RingBuffer<Event> producersRingBuffer) {
        Processor processor = new Processor(producersRingBuffer);
        processor.startNow();
        return processor;
    }

    static void runAsync(RingBuffer<Event> producersRingBuffer) {
        startAsync(producersRingBuffer).waitForCompletion();
    }

    private Processor(RingBuffer<Event> producersRingBuffer) {
        super(TOTAL_ELEMENTS, producersRingBuffer);
    }

    @Override
    String getProfilerName() {
        return "Processor";
    }

    @Override
    void loop() {
        EmptyRingBuffer<Event> producersRingBuffer = getEmptyRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int eventData = producersRingBuffer.take().getData();
            int key = CONSUMERS_RING_BUFFER.nextKey();
            CONSUMERS_RING_BUFFER.next(key).setData(eventData);
            CONSUMERS_RING_BUFFER.put(key);
        }
    }
}
