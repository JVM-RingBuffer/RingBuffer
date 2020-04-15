package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

import static eu.menzani.ringbuffer.RingBufferHelper.*;

class AtomicWriteBlockingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final Integer readPosition;
    private final Integer writePosition;

    AtomicWriteBlockingRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        gcEnabled = builder.isGCEnabled();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        readPosition = builder.newCursor();
        writePosition = builder.newCursor();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T next() {
        return shouldBeAdvancing();
    }

    @Override
    public void put() {
       shouldBeAdvancing();
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition.getPlain();
        int newWritePosition;
        if (writePosition == 0) {
            newWritePosition = capacityMinusOne;
        } else {
            newWritePosition = writePosition - 1;
        }
        writeBusyWaitStrategy.reset();
        while (readPosition.get() == newWritePosition) {
            writeBusyWaitStrategy.tick();
        }
        buffer[writePosition] = element;
        this.writePosition.set(newWritePosition);
    }

    @Override
    public T take() {
        int readPosition = this.readPosition.getPlain();
        readBusyWaitStrategy.reset();
        while (writePosition.get() == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == 0) {
            this.readPosition.set(capacityMinusOne);
        } else {
            this.readPosition.set(readPosition - 1);
        }
        T element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        return element;
    }

    @Override
    public void fill(T[] buffer) {
        int readPosition = this.readPosition.getPlain();
        readBusyWaitStrategy.reset();
        while (size(readPosition) < buffer.length) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition >= buffer.length) {
            int newReadPosition = readPosition - buffer.length;
            for (int j = 0; readPosition > newReadPosition; readPosition--) {
                buffer[j++] = this.buffer[readPosition];
                if (gcEnabled) {
                    this.buffer[readPosition] = null;
                }
            }
            this.readPosition.set(newReadPosition);
        } else {
            fillSplit(readPosition, buffer);
        }
    }

    private void fillSplit(int readPosition, T[] buffer) {
        int j = 0;
        int newReadPosition = readPosition + capacity - buffer.length;
        for (; readPosition >= 0; readPosition--) {
            buffer[j++] = this.buffer[readPosition];
            if (gcEnabled) {
                this.buffer[readPosition] = null;
            }
        }
        for (readPosition = capacityMinusOne; readPosition > newReadPosition; readPosition--) {
            buffer[j++] = this.buffer[readPosition];
            if (gcEnabled) {
                this.buffer[readPosition] = null;
            }
        }
        this.readPosition.set(newReadPosition);
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int readPosition, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            action.accept(buffer[i]);
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            action.accept(buffer[i]);
        }
    }

    @Override
    public boolean contains(T element) {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            if (buffer[i].equals(element)) {
                return true;
            }
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            if (buffer[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size(readPosition.get());
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(readPosition.get(), writePosition.get());
    }

    private boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (isEmpty(readPosition, writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition < readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                builder.append(buffer[i].toString());
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
        for (int i = readPosition; i >= 0; i--) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
    }
}
