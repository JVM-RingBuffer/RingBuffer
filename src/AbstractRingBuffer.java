package eu.menzani.ringbuffer;

public abstract class AbstractRingBuffer<T> implements RingBuffer<T> {
    final int capacity;
    final int capacityMinusOne;
    final Object[] buffer;
    final boolean gc;

    protected AbstractRingBuffer(RingBufferOptions<?> options) {
        capacity = options.getCapacity();
        capacityMinusOne = options.getCapacityMinusOne();
        buffer = options.newBuffer();
        gc = options.getGC();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}
