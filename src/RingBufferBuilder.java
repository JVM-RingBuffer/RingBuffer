package eu.menzani.ringbuffer;

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
    private boolean gc;

    RingBufferBuilder(int capacity, Supplier<? extends T> filler, T dummyElement) {
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
        blocking(new HintBusyWaitStrategy());
        return this;
    }

    public RingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        type = RingBufferType.BLOCKING;
        writeBusyWaitStrategy = busyWaitStrategy;
        return this;
    }

    public RingBufferBuilder<T> discarding() {
        type = RingBufferType.DISCARDING;
        return this;
    }

    public RingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        readBusyWaitStrategy = busyWaitStrategy;
        return this;
    }

    public RingBufferBuilder<T> withGC() {
        gc = true;
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
            throw new IllegalArgumentException("A ring buffer does not support many readers and writers. Consider using a blocking queue instead.");
        }
        if (oneReader) {
            if (!oneWriter && !isPrefilled()) {
                switch (type) {
                    case OVERWRITING:
                        return new OneReaderManyWritersRingBuffer<>(this);
                    case BLOCKING:
                        return new OneReaderManyWritersBlockingOrDiscardingRingBuffer<>(OneReaderOneWriterBlockingOrDiscardingRingBuffer.blocking(this));
                    case DISCARDING:
                        return new OneReaderManyWritersBlockingOrDiscardingRingBuffer<>(OneReaderOneWriterBlockingOrDiscardingRingBuffer.discarding(this));
                }
            }
            switch (type) {
                case OVERWRITING:
                    return new OneReaderOneWriterRingBuffer<>(this);
                case BLOCKING:
                    return OneReaderOneWriterBlockingOrDiscardingRingBuffer.blocking(this);
                case DISCARDING:
                    return OneReaderOneWriterBlockingOrDiscardingRingBuffer.discarding(this);
            }
        }
        switch (type) {
            case OVERWRITING:
                return new ManyReadersOneWriterRingBuffer<>(this);
            case BLOCKING:
                return new ManyReadersOneWriterBlockingOrDiscardingRingBuffer<>(OneReaderOneWriterBlockingOrDiscardingRingBuffer.blocking(this));
            case DISCARDING:
                return new ManyReadersOneWriterBlockingOrDiscardingRingBuffer<>(OneReaderOneWriterBlockingOrDiscardingRingBuffer.discarding(this));
        }
        throw new AssertionError();
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
        if (isPrefilled()) {
            for (int i = 0; i < capacity; i++) {
                buffer[i] = filler.get();
            }
        }
        return buffer;
    }

    boolean getGC() {
        if (!isPrefilled()) {
            return gc;
        }
        if (gc) {
            throw new IllegalArgumentException("A pre-filled ring buffer cannot be garbage collected.");
        }
        return false;
    }

    BusyWaitStrategy getWriteBusyWaitStrategy() {
        return writeBusyWaitStrategy;
    }

    BusyWaitStrategy getReadBusyWaitStrategy() {
        if (readBusyWaitStrategy == null) {
            return new HintBusyWaitStrategy();
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
