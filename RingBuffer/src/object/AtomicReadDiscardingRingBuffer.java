package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

@Contended
class AtomicReadDiscardingRingBuffer<T> implements RingBuffer<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = AtomicReadDiscardingRingBuffer.class;
        READ_POSITION = Lang.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Lang.objectFieldOffset(clazz, "writePosition");
    }

    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private int readPosition;
    @Contended("write")
    private int writePosition;
    @Contended("write")
    private int cachedReadPosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicReadDiscardingRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition;
        int newWritePosition;
        if (writePosition == 0) {
            newWritePosition = capacityMinusOne;
        } else {
            newWritePosition = writePosition - 1;
        }
        if (isNotFullCached(newWritePosition)) {
            AtomicArray.setPlain(buffer, writePosition, element);
            AtomicInt.setRelease(this, WRITE_POSITION, newWritePosition);
        }
    }

    private boolean isNotFullCached(int writePosition) {
        if (cachedReadPosition == writePosition) {
            cachedReadPosition = AtomicInt.getAcquire(this, READ_POSITION);
            return cachedReadPosition != writePosition;
        }
        return true;
    }

    @Override
    public synchronized T take() {
        int readPosition = this.readPosition;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while (isEmptyCached(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == 0) {
            AtomicInt.setRelease(this, READ_POSITION, capacityMinusOne);
        } else {
            AtomicInt.setRelease(this, READ_POSITION, readPosition - 1);
        }
        return AtomicArray.getPlain(buffer, readPosition);
    }

    private boolean isEmptyCached(int readPosition) {
        if (cachedWritePosition == readPosition) {
            cachedWritePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
            return cachedWritePosition == readPosition;
        }
        return false;
    }

    @Override
    public Object getReadMonitor() {
        return this;
    }

    @Override
    public void takeBatch(int size) {
        int readPosition = this.readPosition;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
    }

    @Override
    public T takePlain() {
        int readPosition = this.readPosition;
        if (readPosition == 0) {
            AtomicInt.setRelease(this, READ_POSITION, capacityMinusOne);
        } else {
            AtomicInt.setRelease(this, READ_POSITION, readPosition - 1);
        }
        return AtomicArray.getPlain(buffer, readPosition);
    }

    @Override
    public synchronized T takeLast() {
        int position;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while ((position = AtomicInt.getAcquire(this, WRITE_POSITION)) == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (position == capacityMinusOne) {
            position = 0;
        } else {
            position++;
        }
        readPosition = position;
        return AtomicArray.getPlain(buffer, position);
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = AtomicInt.getAcquire(this, READ_POSITION);
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                action.accept(AtomicArray.getPlain(buffer, readPosition));
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            action.accept(AtomicArray.getPlain(buffer, readPosition));
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            action.accept(AtomicArray.getPlain(buffer, readPosition));
        }
    }

    @Override
    public boolean contains(T element) {
        int readPosition = AtomicInt.getAcquire(this, READ_POSITION);
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                if (AtomicArray.getPlain(buffer, readPosition).equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            if (AtomicArray.getPlain(buffer, readPosition).equals(element)) {
                return true;
            }
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            if (AtomicArray.getPlain(buffer, readPosition).equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size(AtomicInt.getAcquire(this, READ_POSITION));
    }

    private int size(int readPosition) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(AtomicInt.getAcquire(this, READ_POSITION), AtomicInt.getAcquire(this, WRITE_POSITION));
    }

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = AtomicInt.getAcquire(this, READ_POSITION);
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (isEmpty(readPosition, writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (writePosition < readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
                builder.append(", ");
            }
        } else {
            toStringSplit(builder, readPosition, writePosition);
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
            builder.append(", ");
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
            builder.append(", ");
        }
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
