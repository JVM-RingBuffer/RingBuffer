package test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractRingBufferTest extends Benchmark {
    protected static final int NUM_ITERATIONS = 1_000_000;
    protected static final int CONCURRENCY = 3;
    protected static final int TOTAL_ELEMENTS = NUM_ITERATIONS * CONCURRENCY;

    protected static final long ONE_TO_ONE_SUM = getOneToOneSum();
    protected static final long ONE_TO_MANY_SUM = getOneToManySum();
    protected static final long MANY_WRITERS_SUM = ONE_TO_ONE_SUM * CONCURRENCY;

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

    protected abstract long getSum();

    protected abstract long testSum();

    @Override
    final protected int getNumIterations() {
        return 0;
    }

    @Override
    final protected void test(int i) {
        TestThread.resetThreadSpreader();

        assertEquals(getSum(), testSum());
    }
}
