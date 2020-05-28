package test.marshalling;

import org.ringbuffer.java.Number;
import org.ringbuffer.marshalling.Offsets;
import test.AbstractRingBufferTest;

abstract class RingBufferTest extends AbstractRingBufferTest {
    static final int BLOCKING_SIZE = Number.getNextPowerOfTwo(5 * Offsets.INT);
    static final int ONE_TO_ONE_SIZE = Number.getNextPowerOfTwo(NUM_ITERATIONS * Offsets.INT + 1);
    static final int NOT_ONE_TO_ONE_SIZE = Number.getNextPowerOfTwo(TOTAL_ELEMENTS * Offsets.INT + 1);
}
