package eu.menzani.ringbuffer.heap;

import eu.menzani.ringbuffer.java.IntPair;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SparseHeap {
    private static final int blockSize = 4;

    private final ByteBuffer memory;
    private final MemorySize size;
    private final int[] references;
    private int position;

    public SparseHeap(MemorySize size) {
        memory = ByteBuffer.allocateDirect(size.getBytes()).order(ByteOrder.nativeOrder());
        this.size = size;

        int stackSize = memory.capacity() / blockSize;
        references = new int[stackSize];
        position = stackSize - 1;
        for (int i = position; i >= 0; i--) {
            references[i] = i * blockSize;
        }
    }

    public MemorySize getSize() {
        return size;
    }

    public MemorySize freeMemory() {
        return size.subtract(usedMemory());
    }

    public MemorySize usedMemory() {
        return MemorySize.ofBytes((references.length - position - 1) * blockSize);
    }

    public int allocateByte() {
        return allocate((byte) 0);
    }

    public int allocateChar() {
        return allocate((char) 0);
    }

    public int allocateShort() {
        return allocate((short) 0);
    }

    public int allocateInt() {
        return allocate(0);
    }

    public long allocateLong() {
        return allocate(0L);
    }

    public int allocateBoolean() {
        return allocate(false);
    }

    public int allocateFloat() {
        return allocate(0F);
    }

    public long allocateDouble() {
        return allocateLong();
    }

    public int allocate(byte value) {
        int ref = popReference();
        set(ref, value);
        return ref;
    }

    public int allocate(char value) {
        int ref = popReference();
        set(ref, value);
        return ref;
    }

    public int allocate(short value) {
        int ref = popReference();
        set(ref, value);
        return ref;
    }

    public int allocate(int value) {
        int ref = popReference();
        set(ref, value);
        return ref;
    }

    public long allocate(long value) {
        int ref1 = popReference();
        int ref2 = popReference();
        memory.putInt(ref1, IntPair.getFirst(value));
        memory.putInt(ref2, IntPair.getSecond(value));
        return IntPair.of(ref1, ref2);
    }

    public int allocate(boolean value) {
        int ref = popReference();
        set(ref, value);
        return ref;
    }

    public int allocate(float value) {
        int ref = popReference();
        set(ref, value);
        return ref;
    }

    public long allocate(double value) {
        return allocate(Double.doubleToLongBits(value));
    }

    private int popReference() {
        return references[--position];
    }

    public void deallocate(int ref) {
        references[position++] = ref;
    }

    public void deallocate(long ref) {
        deallocate(IntPair.getFirst(ref));
        deallocate(IntPair.getSecond(ref));
    }

    public void set(int ref, byte value) {
        memory.put(ref, value);
    }

    public void set(int ref, char value) {
        memory.putChar(ref, value);
    }

    public void set(int ref, short value) {
        memory.putShort(ref, value);
    }

    public void set(int ref, int value) {
        memory.putInt(ref, value);
    }

    public void set(long ref, long value) {
        set(IntPair.getFirst(ref), IntPair.getFirst(value));
        set(IntPair.getSecond(ref), IntPair.getSecond(value));
    }

    public void set(int ref, boolean value) {
        set(ref, value ? (byte) 1 : (byte) 0);
    }

    public void set(int ref, float value) {
        memory.putFloat(ref, value);
    }

    public void set(long ref, double value) {
        set(ref, Double.doubleToLongBits(value));
    }

    public byte getByte(int ref) {
        return memory.get(ref);
    }

    public char getChar(int ref) {
        return memory.getChar(ref);
    }

    public short getShort(int ref) {
        return memory.getShort(ref);
    }

    public int getInt(int ref) {
        return memory.getInt(ref);
    }

    public long getLong(long ref) {
        int first = getInt(IntPair.getFirst(ref));
        int second = getInt(IntPair.getSecond(ref));
        return IntPair.of(first, second);
    }

    public boolean getBoolean(int ref) {
        return getByte(ref) == (byte) 1;
    }

    public float getFloat(int ref) {
        return memory.getFloat(ref);
    }

    public double getDouble(long ref) {
        return Double.longBitsToDouble(getLong(ref));
    }
}
