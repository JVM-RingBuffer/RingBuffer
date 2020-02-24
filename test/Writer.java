package eu.menzani.ringbuffer;

class Writer extends TestThread {
    Writer(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void tick(int i) {
        Event event = new Event();
        event.setData(i);
        ringBuffer.put(event);
    }
}
