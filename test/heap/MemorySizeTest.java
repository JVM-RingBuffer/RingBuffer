package eu.menzani.ringbuffer.heap;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class MemorySizeTest {
    @Test
    void getUnit() {
        assertSame(MemorySize.Unit.KILOBYTE, MemorySize.ofBytes(1025).getUnit());
        assertSame(MemorySize.Unit.MEGABYTE, MemorySize.ofGigabytes(0.15).getUnit());
    }

    @Test
    void getNextPowerOfTwo() {
        MemorySize fourKilobytes = MemorySize.ofKilobytes(4);
        MemorySize eightPetabytes = MemorySize.ofPetabytes(8);

        assertSame(fourKilobytes, fourKilobytes.getNextPowerOfTwo());
        assertEquals(eightPetabytes, MemorySize.ofPetabytes(4.9).getNextPowerOfTwo());
        assertEquals(eightPetabytes, MemorySize.ofPetabytes(5.1).getNextPowerOfTwo());
    }

    @Test
    void serialization() throws IOException, ClassNotFoundException {
        MemorySize memorySize = MemorySize.ofGigabytes(8.1).plusMegabytes(900);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (ObjectOutput output = new ObjectOutputStream(stream)) {
            output.writeObject(memorySize);
        }
        MemorySize object;
        try (ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(stream.toByteArray()))) {
            object = (MemorySize) input.readObject();
        }
        assertEquals(memorySize, object);
    }
}
