package test;

import eu.menzani.ringbuffer.RingBuffer;

class Processor extends TestThread {
    static Processor startAsync(int numIterations, RingBuffer<Event> producersRingBuffer, RingBuffer<Event> consumersRingBuffer) {
        Processor processor = new Processor(numIterations, producersRingBuffer, consumersRingBuffer);
        processor.start();
        return processor;
    }

    static void runAsync(int numIterations, RingBuffer<Event> producersRingBuffer, RingBuffer<Event> consumersRingBuffer) {
        startAsync(numIterations, producersRingBuffer, consumersRingBuffer).reportPerformance();
    }

    private Processor(int numIterations, RingBuffer<Event> producersRingBuffer, RingBuffer<Event> consumersRingBuffer) {
        super(numIterations, producersRingBuffer, consumersRingBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        RingBuffer<Event> consumersRingBuffer = getRingBuffer2();
        RingBuffer<Event> producersRingBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            consumersRingBuffer.put(producersRingBuffer.take());
        }
    }
}
