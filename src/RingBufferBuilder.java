package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.HintBusyWaitStrategy;

import java.util.function.Supplier;

public class RingBufferBuilder<T> {
    private final int capacity;
    private final Supplier<? extends T> filler;
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
        this.dummyElement = dummyElement;
    }

    public RingBufferBuilder<T> oneWriter() {
        oneWriter = true;
        return this;
    }

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
            throw new IllegalArgumentException("A ring buffer does not support many readers and writers. Consider using a concurrent queue instead.");
        }
        if (oneReader) {
            if (!oneWriter && (!isPrefilled() || type == RingBufferType.BLOCKING)) {
                switch (type) {
                    case OVERWRITING:
                        return new AtomicWriteRingBuffer<>(this);
                    case BLOCKING:
                        if (isPrefilled()) {
                            return new DisposableAtomicWriteBlockingPrefilledRingBuffer<>(this);
                        }
                        return new AtomicWriteBlockingOrDiscardingRingBuffer<>(this, false);
                    case DISCARDING:
                        return new AtomicWriteBlockingOrDiscardingRingBuffer<>(this, true);
                }
            }
            switch (type) {
                case OVERWRITING:
                    return new VolatileRingBuffer<>(this);
                case BLOCKING:
                    if (isPrefilled()) {
                        return new DisposableVolatileBlockingPrefilledRingBuffer<>(this);
                    }
                    return new VolatileBlockingOrDiscardingRingBuffer<>(this, false);
                case DISCARDING:
                    return new VolatileBlockingOrDiscardingRingBuffer<>(this, true);
            }
        }
        switch (type) {
            case OVERWRITING:
                return new AtomicReadRingBuffer<>(this);
            case BLOCKING:
                if (isPrefilled()) {
                    return new DisposableAtomicReadBlockingPrefilledRingBuffer<>(this);
                }
                return new AtomicReadBlockingOrDiscardingRingBuffer<>(this, false);
            case DISCARDING:
                return new AtomicReadBlockingOrDiscardingRingBuffer<>(this, true);
        }
        throw new AssertionError();
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    Object[] newBuffer() {
        Object[] buffer = new Object[capacity];
        if (isPrefilled()) {
            for (int i = 0; i < capacity; i++) {
                buffer[i] = filler.get();
            }
        }
        return buffer;
    }

    boolean isGCEnabled() {
        if (!isPrefilled()) {
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
        if (isPrefilled()) {
            return filler.get();
        }
        return dummyElement;
    }

    private boolean isPrefilled() {
        return filler != null;
    }

    private enum RingBufferType {
        OVERWRITING,
        BLOCKING,
        DISCARDING
    }
}
