package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Int;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.HintBusyWaitStrategy;

import java.util.function.Supplier;

public class RingBufferBuilder<T> {
    private int capacity;
    private final Supplier<? extends T> filler;
    private final boolean isPrefilled;
    private final T dummyElement;
    private Boolean oneWriter;
    private Boolean oneReader;
    private RingBufferType type = RingBufferType.OVERWRITING;
    private BusyWaitStrategy writeBusyWaitStrategy;
    private BusyWaitStrategy readBusyWaitStrategy;
    private boolean gcEnabled;

    RingBufferBuilder(int capacity, Supplier<? extends T> filler, T dummyElement) {
        Assume.notLesser(capacity, 2, "capacity");
        this.capacity = capacity;
        this.filler = filler;
        isPrefilled = filler != null;
        this.dummyElement = dummyElement;
    }

    public RingBufferBuilder<T> oneWriter() {
        oneWriter = true;
        return this;
    }

    /**
     * If the ring buffer is not blocking nor discarding, the capacity will be rounded to the next power of 2.
     */
    public RingBufferBuilder<T> manyWriters() {
        oneWriter = false;
        return this;
    }

    public RingBufferBuilder<T> oneReader() {
        oneReader = true;
        return this;
    }

    public RingBufferBuilder<T> manyReaders() {
        oneReader = false;
        return this;
    }

    public RingBufferBuilder<T> blocking() {
        blocking(HintBusyWaitStrategy.getDefault());
        return this;
    }

    public RingBufferBuilder<T> blocking(BusyWaitStrategy strategy) {
        type = RingBufferType.BLOCKING;
        writeBusyWaitStrategy = strategy;
        return this;
    }

    public RingBufferBuilder<T> discarding() {
        type = RingBufferType.DISCARDING;
        return this;
    }

    public RingBufferBuilder<T> waitingWith(BusyWaitStrategy strategy) {
        readBusyWaitStrategy = strategy;
        return this;
    }

    public RingBufferBuilder<T> withGC() {
        gcEnabled = true;
        return this;
    }

    public RingBuffer<T> build() {
        if (oneReader == null && oneWriter == null) {
            switch (type) {
                case OVERWRITING:
                    return new LocalRingBuffer<>(this);
                case DISCARDING:
                    return new LocalDiscardingRingBuffer<>(this);
                case BLOCKING:
                    throw new IllegalArgumentException("A local ring buffer cannot be blocking.");
            }
        }
        if (oneReader == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders().");
        }
        if (oneWriter == null) {
            throw new IllegalStateException("You must call either oneWriter() or manyWriters().");
        }
        if (!oneReader && !oneWriter) {
            throw new IllegalArgumentException("Multiple readers and writers are not supported. Consider using a concurrent queue instead.");
        }
        if (oneReader) {
            if (!oneWriter) {
                switch (type) {
                    case OVERWRITING:
                        capacity = Int.getNextPowerOfTwo(capacity);
                        return new AtomicWriteRingBuffer<>(this);
                    case BLOCKING:
                        if (isPrefilled) {
                            return new AdvancingAtomicWriteBlockingPrefilledRingBuffer<>(this);
                        }
                        return new AtomicWriteBlockingRingBuffer<>(this);
                    case DISCARDING:
                        if (isPrefilled) {
                            break;
                        }
                        return new AtomicWriteDiscardingRingBuffer<>(this);
                }
            }
            switch (type) {
                case OVERWRITING:
                    return new VolatileRingBuffer<>(this);
                case BLOCKING:
                    if (isPrefilled) {
                        return new AdvancingAtomicReadBlockingPrefilledRingBuffer<>(this);
                    }
                    return new VolatileBlockingRingBuffer<>(this);
                case DISCARDING:
                    return new VolatileDiscardingRingBuffer<>(this);
            }
        }
        switch (type) {
            case OVERWRITING:
                return new AtomicReadRingBuffer<>(this);
            case BLOCKING:
                if (isPrefilled) {
                    return new AdvancingAtomicReadBlockingPrefilledRingBuffer<>(this);
                }
                return new AtomicReadBlockingRingBuffer<>(this);
            case DISCARDING:
                return new AtomicReadDiscardingRingBuffer<>(this);
        }
        throw new AssertionError();
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    T[] newBuffer() {
        T[] buffer = (T[]) new Object[capacity];
        if (isPrefilled) {
            for (int i = 0; i < capacity; i++) {
                buffer[i] = filler.get();
            }
        }
        return buffer;
    }

    boolean isGCEnabled() {
        if (!isPrefilled) {
            return gcEnabled;
        }
        if (gcEnabled) {
            throw new IllegalArgumentException("A pre-filled ring buffer cannot be garbage collected.");
        }
        return false;
    }

    BusyWaitStrategy getWriteBusyWaitStrategy() {
        return writeBusyWaitStrategy;
    }

    BusyWaitStrategy getReadBusyWaitStrategy() {
        if (readBusyWaitStrategy == null) {
            return HintBusyWaitStrategy.getDefault();
        }
        return readBusyWaitStrategy;
    }

    T getDummyElement() {
        if (isPrefilled) {
            return filler.get();
        }
        return dummyElement;
    }

    LazyVolatileBooleanArray newFlagArray() {
        return new LazyVolatileBooleanArray(capacity);
    }

    private enum RingBufferType {
        OVERWRITING,
        BLOCKING,
        DISCARDING
    }
}
