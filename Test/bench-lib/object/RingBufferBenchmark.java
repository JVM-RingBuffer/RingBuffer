package bench.object;

import bench.AbstractRingBufferBenchmark;
import eu.menzani.lang.Numbers;
import eu.menzani.object.ObjectFactory;

public abstract class RingBufferBenchmark extends AbstractRingBufferBenchmark {
    static final int BLOCKING_SIZE = 5;
    protected static final int ONE_TO_ONE_SIZE = NUM_ITERATIONS + 1;
    protected static final int NOT_ONE_TO_ONE_SIZE = TOTAL_ELEMENTS + 1;
    protected static final int LOCKFREE_ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(ONE_TO_ONE_SIZE);
    protected static final int LOCKFREE_NOT_ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(NOT_ONE_TO_ONE_SIZE);

    static final int BATCH_SIZE = 20;
    static final int BLOCKING_BATCH_SIZE = 4;

    static final ObjectFactory<Event> FILLER = () -> new Event(0);
}
