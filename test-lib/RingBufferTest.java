package test;

import java.util.function.Supplier;

abstract class RingBufferTest extends AbstractRingBufferTest {
    static final int BLOCKING_SIZE = 5;
    static final int ONE_TO_ONE_SIZE = NUM_ITERATIONS + 1;
    static final int NOT_ONE_TO_ONE_SIZE = TOTAL_ELEMENTS + 1;

    static final int BATCH_SIZE = 20;
    static final int BLOCKING_BATCH_SIZE = 4;

    static final Supplier<Event> FILLER = () -> new Event(0);
}
