package eu.menzani.ringbuffer.heap;

import eu.menzani.ringbuffer.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SparseHeapTest {
    private static final Random random = Random.INSTANCE;
    private static final MemorySize twoKilobytes = MemorySize.ofKilobytes(2);

    private final SparseHeap heap = new SparseHeap(MemorySize.ofMegabytes(2));

    @Test
    void allocateDefaultByte() {
        int ref = heap.allocateByte();
        assertEquals((byte) 0, heap.getByte(ref));
    }

    @Test
    void allocateDefaultChar() {
        int ref = heap.allocateChar();
        assertEquals((char) 0, heap.getChar(ref));
    }

    @Test
    void allocateDefaultShort() {
        int ref = heap.allocateShort();
        assertEquals((short) 0, heap.getShort(ref));
    }

    @Test
    void allocateDefaultInt() {
        int ref = heap.allocateInt();
        assertEquals(0, heap.getInt(ref));
    }

    @Test
    void allocateDefaultLong() {
        long ref = heap.allocateLong();
        assertEquals(0L, heap.getLong(ref));
    }

    @Test
    void allocateDefaultBoolean() {
        int ref = heap.allocateBoolean();
        assertFalse(heap.getBoolean(ref));
    }

    @Test
    void allocateDefaultFloat() {
        int ref = heap.allocateFloat();
        assertEquals(0F, heap.getFloat(ref));
    }

    @Test
    void allocateDefaultDouble() {
        long ref = heap.allocateDouble();
        assertEquals(0D, heap.getDouble(ref));
    }

    @Test
    void allocateByte() {
        byte value = random.nextByte();
        int ref = heap.allocate(value);
        assertEquals(value, heap.getByte(ref));
    }

    @Test
    void allocateChar() {
        char value = random.nextChar();
        int ref = heap.allocate(value);
        assertEquals(value, heap.getChar(ref));
    }

    @Test
    void allocateShort() {
        short value = random.nextShort();
        int ref = heap.allocate(value);
        assertEquals(value, heap.getShort(ref));
    }

    @Test
    void allocateInt() {
        int value = random.nextInt();
        int ref = heap.allocate(value);
        assertEquals(value, heap.getInt(ref));
    }

    @Test
    void allocateLong() {
        long value = random.nextLong();
        long ref = heap.allocate(value);
        assertEquals(value, heap.getLong(ref));
    }

    @Test
    void allocateBoolean() {
        boolean value = random.nextBoolean();
        int ref = heap.allocate(value);
        assertEquals(value, heap.getBoolean(ref));
    }

    @Test
    void allocateFloat() {
        float value = random.nextFloat();
        int ref = heap.allocate(value);
        assertEquals(value, heap.getFloat(ref));
    }

    @Test
    void allocateDouble() {
        double value = random.nextDouble();
        long ref = heap.allocate(value);
        assertEquals(value, heap.getDouble(ref));
    }

    @Test
    void deallocate() {
        SparseHeap heap = new SparseHeap(twoKilobytes);
        MemorySize thirtyTwoBytes = MemorySize.ofBytes(32);

        assertEquals(twoKilobytes, heap.freeMemory());
        assertEquals(MemorySize.ZERO, heap.usedMemory());
        int ref = heap.allocate(random.nextInt());
        assertEquals(twoKilobytes.subtract(thirtyTwoBytes), heap.freeMemory());
        assertEquals(thirtyTwoBytes, heap.usedMemory());
        heap.deallocate(ref);
        assertEquals(twoKilobytes, heap.freeMemory());
        assertEquals(MemorySize.ZERO, heap.usedMemory());
    }

    @Test
    void deallocate64() {
        SparseHeap heap = new SparseHeap(twoKilobytes);
        MemorySize sixtyFourBytes = MemorySize.ofBytes(64);

        assertEquals(twoKilobytes, heap.freeMemory());
        assertEquals(MemorySize.ZERO, heap.usedMemory());
        long ref = heap.allocate(random.nextLong());
        assertEquals(twoKilobytes.subtract(sixtyFourBytes), heap.freeMemory());
        assertEquals(sixtyFourBytes, heap.usedMemory());
        heap.deallocate(ref);
        assertEquals(twoKilobytes, heap.freeMemory());
        assertEquals(MemorySize.ZERO, heap.usedMemory());
    }

    @Test
    void setByte() {
        byte value = random.nextByte();
        int ref = heap.allocateByte();
        heap.set(ref, value);
        assertEquals(value, heap.getByte(ref));
    }

    @Test
    void setChar() {
        char value = random.nextChar();
        int ref = heap.allocateChar();
        heap.set(ref, value);
        assertEquals(value, heap.getChar(ref));
    }

    @Test
    void setShort() {
        short value = random.nextShort();
        int ref = heap.allocateShort();
        heap.set(ref, value);
        assertEquals(value, heap.getShort(ref));
    }

    @Test
    void setInt() {
        int value = random.nextInt();
        int ref = heap.allocateInt();
        heap.set(ref, value);
        assertEquals(value, heap.getInt(ref));
    }

    @Test
    void setLong() {
        long value = random.nextLong();
        long ref = heap.allocateLong();
        heap.set(ref, value);
        assertEquals(value, heap.getLong(ref));
    }

    @Test
    void setBoolean() {
        boolean value = random.nextBoolean();
        int ref = heap.allocateBoolean();
        heap.set(ref, value);
        assertEquals(value, heap.getBoolean(ref));
    }

    @Test
    void setFloat() {
        float value = random.nextFloat();
        int ref = heap.allocateFloat();
        heap.set(ref, value);
        assertEquals(value, heap.getFloat(ref));
    }

    @Test
    void setDouble() {
        double value = random.nextDouble();
        long ref = heap.allocateDouble();
        heap.set(ref, value);
        assertEquals(value, heap.getDouble(ref));
    }
}
