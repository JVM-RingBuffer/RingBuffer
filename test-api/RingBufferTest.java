package test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

interface RingBufferTest {
    int NUM_ITERATIONS = 1_000_000;
    int CONCURRENCY = 5;
    int TOTAL_ELEMENTS = NUM_ITERATIONS * CONCURRENCY;

    long ONE_TO_ONE_SUM = getOneToOneSum();
    long ONE_TO_MANY_SUM = getOneToManySum();
    long MANY_WRITERS_SUM = ONE_TO_ONE_SUM * CONCURRENCY;

    private static long getOneToOneSum() {
        long result = 0L;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            result += i;
        }
        return result;
    }

    private static long getOneToManySum() {
        long result = 0L;
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            result += i;
        }
        return result;
    }

    int BLOCKING_SIZE = 5;
    int ONE_TO_ONE_SIZE = NUM_ITERATIONS + 1;
    int NOT_ONE_TO_ONE_SIZE = TOTAL_ELEMENTS + 1;

    int BATCH_SIZE = 20;
    int BLOCKING_BATCH_SIZE = 4;

    Supplier<Event> FILLER = () -> new Event(0);
    Benchmark BENCHMARK = new Benchmark();

    int getBenchmarkRepeatTimes();

    long getSum();

    long run();

    default void runTest() {
        test();
        BENCHMARK.begin();
        test();
        BENCHMARK.report();
    }

    private void test() {
        for (int i = 0; i < getBenchmarkRepeatTimes(); i++) {
            assertEquals(getSum(), run());
        }
    }
}
