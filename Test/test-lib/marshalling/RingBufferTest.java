package test.marshalling;

import eu.menzani.lang.Numbers;
import eu.menzani.struct.HeapOffsets;
import test.AbstractRingBufferTest;

abstract class RingBufferTest extends AbstractRingBufferTest {
    static final int BLOCKING_SIZE = Numbers.getNextPowerOfTwo(5 * HeapOffsets.INT);
    static final int ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(NUM_ITERATIONS * HeapOffsets.INT + 1);
    static final int NOT_ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(TOTAL_ELEMENTS * HeapOffsets.INT + 1);
}
