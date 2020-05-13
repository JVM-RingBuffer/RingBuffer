package test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

abstract class RingBufferTest extends Benchmark {
    static final int NUM_ITERATIONS = 1_000_000;
    static final int CONCURRENCY = 5;
    static final int TOTAL_ELEMENTS = NUM_ITERATIONS * CONCURRENCY;

    static final long ONE_TO_ONE_SUM = getOneToOneSum();
    static final long ONE_TO_MANY_SUM = getOneToManySum();
    static final long MANY_WRITERS_SUM = ONE_TO_ONE_SUM * CONCURRENCY;

    private static long getOneToOneSum() {
        long result = 0L;
        for (int i = 1; i <= NUM_ITERATIONS; i++) {
            result += i;
        }
        return result;
    }

    private static long getOneToManySum() {
        long result = 0L;
        for (int i = 1; i <= TOTAL_ELEMENTS; i++) {
            result += i;
        }
        return result;
    }

    static final int BLOCKING_SIZE = 5;
    static final int ONE_TO_ONE_SIZE = NUM_ITERATIONS + 1;
    static final int NOT_ONE_TO_ONE_SIZE = TOTAL_ELEMENTS + 1;

    static final int BATCH_SIZE = 20;
    static final int BLOCKING_BATCH_SIZE = 4;

    static final Supplier<Event> FILLER = () -> new Event(0);

    abstract long getSum();

    abstract long testSum();

    @Override
    protected int getNumIterations() {
        return 0;
    }

    @Override
    protected void test(int i) {
        assertEquals(getSum(), testSum());
    }
}
