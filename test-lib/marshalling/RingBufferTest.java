package test.marshalling;

import eu.menzani.ringbuffer.java.Number;
import eu.menzani.ringbuffer.marshalling.Offsets;
import test.AbstractRingBufferTest;

abstract class RingBufferTest extends AbstractRingBufferTest {
    static final int BLOCKING_SIZE = 5;
    static final int ONE_TO_ONE_SIZE = Number.getNextPowerOfTwo(NUM_ITERATIONS * Offsets.INT + 1);
    static final int NOT_ONE_TO_ONE_SIZE = Number.getNextPowerOfTwo(TOTAL_ELEMENTS * Offsets.INT + 1);

    static final int BATCH_SIZE = 20;
    static final int BLOCKING_BATCH_SIZE = 4;
}
