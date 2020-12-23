package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

@Contended
class ConcurrentRingBuffer<T> implements RingBuffer<T> {
    private static final long WRITE_POSITION = Lang.objectFieldOffset(ConcurrentRingBuffer.class, "writePosition");

    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private int readPosition;
    @Contended
    private int writePosition;
    @Contended("read")
    private int cachedWritePosition;

    ConcurrentRingBuffer(RingBufferBuilder<T> builder) {
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
    public synchronized void put(T element) {
        int writePosition = this.writePosition;
        AtomicArray.setPlain(buffer, writePosition, element);
        if (writePosition == 0) {
            AtomicInt.setRelease(this, WRITE_POSITION, capacityMinusOne);
        } else {
            AtomicInt.setRelease(this, WRITE_POSITION, writePosition - 1);
        }
    }

    @Override
    public T take() {
        int readPosition;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        synchronized (readBusyWaitStrategy) {
            readPosition = this.readPosition;
            readBusyWaitStrategy.reset();
            while (isEmptyCached(readPosition)) {
                readBusyWaitStrategy.tick();
            }
            if (readPosition == 0) {
                this.readPosition = capacityMinusOne;
            } else {
                this.readPosition--;
            }
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
        return readBusyWaitStrategy;
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
            this.readPosition = capacityMinusOne;
        } else {
            this.readPosition--;
        }
        return AtomicArray.getPlain(buffer, readPosition);
    }

    @Override
    public T takeLast() {
        int position;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        synchronized (readBusyWaitStrategy) {
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
        }
        return AtomicArray.getPlain(buffer, position);
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = getReadPosition();
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(AtomicArray.getPlain(buffer, i));
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
        int readPosition = getReadPosition();
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (AtomicArray.getPlain(buffer, i).equals(element)) {
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
        return size(getReadPosition());
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
        return isEmpty(getReadPosition(), AtomicInt.getAcquire(this, WRITE_POSITION));
    }

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = getReadPosition();
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (isEmpty(readPosition, writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (writePosition < readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                builder.append(AtomicArray.getPlain(buffer, i).toString());
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

    private int getReadPosition() {
        synchronized (readBusyWaitStrategy) {
            return readPosition;
        }
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
