package eu.menzani.ringbuffer;

class PrefilledWriter extends TestThread {
    PrefilledWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        for (int i = 0; i < numIterations; i++) {
            Event event = ringBuffer.next();
            event.setData(i);
            ringBuffer.put();
        }
    }
}
