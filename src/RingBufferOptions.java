package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.HintBusyWaitStrategy;

import java.util.function.Supplier;

public class RingBufferOptions<T> {
    public static RingBufferOptions<?> emptyBuffer(int capacity) {
        RingBufferOptions<?> options = new RingBufferOptions<>();
        options.capacity = capacity;
        return options;
    }

    public static <T> RingBufferOptions<T> emptyBuffer(int capacity, T dummyElement) {
        RingBufferOptions<T> options = new RingBufferOptions<>();
        options.capacity = capacity;
        options.dummyElement = dummyElement;
        return options;
    }

    public static <T> RingBufferOptions<T> prefilledBuffer(int capacity, Supplier<? extends T> filler) {
        RingBufferOptions<T> options = new RingBufferOptions<>();
        options.capacity = capacity;
        options.filler = filler;
        return options;
    }

    private int capacity;
    private Supplier<? extends T> filler;
    private T dummyElement;
    private BusyWaitStrategy writeBusyWaitStrategy;
    private BusyWaitStrategy readBusyWaitStrategy;
    private boolean gc;

    public RingBufferOptions<T> withWriteBusyWaitStrategy(BusyWaitStrategy writeBusyWaitStrategy) {
        this.writeBusyWaitStrategy = writeBusyWaitStrategy;
        return this;
    }

    public RingBufferOptions<T> withReadBusyWaitStrategy(BusyWaitStrategy readBusyWaitStrategy) {
        this.readBusyWaitStrategy = readBusyWaitStrategy;
        return this;
    }

    public RingBufferOptions<T> withGC() {
        gc = true;
        return this;
    }

    int getCapacity() {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    Object[] newBuffer() {
        Object[] buffer = new Object[capacity];
        if (filler != null) {
            for (int i = 0; i < capacity; i++) {
                buffer[i] = filler.get();
            }
        }
        return buffer;
    }

    boolean getGC() {
        if (filler == null) {
            return gc;
        }
        if (gc) {
            throw new IllegalArgumentException("A pre-filled ring buffer cannot be garbage collected.");
        }
        return false;
    }

    BusyWaitStrategy getWriteBusyWaitStrategy() {
        if (writeBusyWaitStrategy == null) {
            return new HintBusyWaitStrategy();
        }
        return writeBusyWaitStrategy;
    }

    BusyWaitStrategy getReadBusyWaitStrategy() {
        if (readBusyWaitStrategy == null) {
            return new HintBusyWaitStrategy();
        }
        return readBusyWaitStrategy;
    }

    T getDummyElement() {
        if (filler != null) {
            return filler.get();
        }
        return dummyElement;
    }
}
