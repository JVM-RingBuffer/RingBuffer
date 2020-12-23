package org.ringbuffer.marshalling;

import eu.menzani.concurrent.ThreadLocal;
import org.ringbuffer.AbstractRingBuffer;
import org.ringbuffer.wait.BusyWaitStrategy;

interface AbstractDirectRingBuffer extends AbstractRingBuffer {
    long getCapacity();

    /**
     * If the ring buffer is lock-free, then {@code offset} is the value returned by
     * {@link DirectRingBuffer#next(long)}.
     */
    void put(long offset);

    long take(long size, @ThreadLocal BusyWaitStrategy busyWaitStrategy);

    /**
     * If the ring buffer supports multiple readers and is not lock-free, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     long offset = ringBuffer.take(...);
     *     // Read data
     *     ringBuffer.advance(...); // If needed
     * }
     * }</pre>
     */
    long take(long size);

    long size();

    byte readByte(long offset);

    char readChar(long offset);

    short readShort(long offset);

    int readInt(long offset);

    long readLong(long offset);

    boolean readBoolean(long offset);

    float readFloat(long offset);

    double readDouble(long offset);

    void writeByte(long offset, byte value);

    void writeChar(long offset, char value);

    void writeShort(long offset, short value);

    void writeInt(long offset, int value);

    void writeLong(long offset, long value);

    void writeBoolean(long offset, boolean value);

    void writeFloat(long offset, float value);

    void writeDouble(long offset, double value);
}
