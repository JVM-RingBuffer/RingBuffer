package org.ringbuffer.marshalling;

import eu.menzani.concurrent.ThreadLocal;
import org.ringbuffer.AbstractRingBuffer;
import org.ringbuffer.wait.BusyWaitStrategy;

interface AbstractHeapRingBuffer extends AbstractRingBuffer {
    int getCapacity();

    /**
     * If the ring buffer is lock-free, then {@code offset} is the value returned by
     * {@link HeapRingBuffer#next(int)}.
     */
    void put(int offset);

    int take(int size, @ThreadLocal BusyWaitStrategy busyWaitStrategy);

    /**
     * If the ring buffer supports multiple readers and is not lock-free, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     int offset = ringBuffer.take(...);
     *     // Read data
     *     ringBuffer.advance(...); // If needed
     * }
     * }</pre>
     */
    int take(int size);

    int size();

    void writeByte(int offset, byte value);

    void writeChar(int offset, char value);

    void writeShort(int offset, short value);

    void writeInt(int offset, int value);

    void writeLong(int offset, long value);

    void writeBoolean(int offset, boolean value);

    void writeFloat(int offset, float value);

    void writeDouble(int offset, double value);

    byte readByte(int offset);

    char readChar(int offset);

    short readShort(int offset);

    int readInt(int offset);

    long readLong(int offset);

    boolean readBoolean(int offset);

    float readFloat(int offset);

    double readDouble(int offset);
}
