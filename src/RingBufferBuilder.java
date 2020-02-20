package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.HintBusyWaitStrategy;

import java.util.function.Supplier;

public class RingBufferBuilder {
    private final int capacity;
    private final Supplier<?> filler;
    private final Object dummyElement;
    private Boolean oneReader;
    private Boolean oneWriter;
    private RingBufferType type = RingBufferType.OVERWRITING;
    private BusyWaitStrategy writeBusyWaitStrategy;
    private BusyWaitStrategy readBusyWaitStrategy;
    private boolean gc;

    RingBufferBuilder(int capacity, Supplier<?> filler, Object dummyElement) {
        this.capacity = capacity;
        this.filler = filler;
        this.dummyElement = dummyElement;
    }

    public RingBufferBuilder oneReader() {
        oneReader = true;
        return this;
    }

    public RingBufferBuilder manyReaders() {
        oneReader = false;
        return this;
    }

    public RingBufferBuilder oneWriter() {
        oneWriter = true;
        return this;
    }

    public RingBufferBuilder manyWriters() {
        oneWriter = false;
        return this;
    }

    public RingBufferBuilder blocking() {
        type = RingBufferType.BLOCKING;
        return this;
    }

    public RingBufferBuilder discarding() {
        type = RingBufferType.DISCARDING;
        return this;
    }

    public RingBufferBuilder withWriteBusyWaitStrategy(BusyWaitStrategy writeBusyWaitStrategy) {
        this.writeBusyWaitStrategy = writeBusyWaitStrategy;
        return this;
    }

    public RingBufferBuilder withReadBusyWaitStrategy(BusyWaitStrategy readBusyWaitStrategy) {
        this.readBusyWaitStrategy = readBusyWaitStrategy;
        return this;
    }

    public RingBufferBuilder withGC() {
        gc = true;
        return this;
    }

    public <T> RingBuffer<T> build() {
        if (oneReader == null && oneWriter == null) {
            switch (type) {
                case OVERWRITING:
                    return new LocalRingBuffer<>(this);
                case DISCARDING:
                    return new LocalDiscardingRingBuffer<>(this);
                case BLOCKING:
                    throw new IllegalArgumentException("A local ring buffer is not blocking.");
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

    Object getDummyElement() {
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
