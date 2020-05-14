package eu.menzani.ringbuffer.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadSpreaderTest {
    @Test
    void testNonCyclying() {
        ThreadSpreader spreader = Threads.spreadOverCPUs().fromFirstCPU().toCPU(5).skipHyperthreads().build();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
        spreader.reset();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
        assertThrows(ThreadManipulationException.class, spreader::nextCPU);
    }

    @Test
    void testCyclying() {
        ThreadSpreader spreader = Threads.spreadOverCPUs().fromFirstCPU().toCPU(5).skipHyperthreads().cycle().build();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        spreader.reset();
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
        assertEquals(4, spreader.nextCPU());
        assertEquals(0, spreader.nextCPU());
        assertEquals(2, spreader.nextCPU());
    }
}
