package eu.menzani.ringbuffer;

import java.util.function.Supplier;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

public interface Test {
    int NUM_ITERATIONS = 1_000_000;
    int CONCURRENCY = 6;
    int TOTAL_ELEMENTS = NUM_ITERATIONS * CONCURRENCY;

    long ONE_TO_ONE_SUM = LongStream.range(0L, NUM_ITERATIONS).sum();
    long MANY_READERS_SUM = LongStream.range(0L, TOTAL_ELEMENTS).sum();
    long MANY_WRITERS_SUM = ONE_TO_ONE_SUM * CONCURRENCY;

    int BLOCKING_SIZE = 5;
    int ONE_TO_ONE_SIZE = NUM_ITERATIONS + 1;
    int MANY_READERS_OR_WRITERS_SIZE = TOTAL_ELEMENTS + 1;
    Supplier<Event> FILLER = () -> new Event(0);

    long run();

    default void runTest(int benchmarkRepeatTimes, long sum) {
        int repeatTimes = Benchmark.isEnabled() ? benchmarkRepeatTimes : 1;
        for (int i = 0; i < repeatTimes; i++) {
            assertEquals(sum, run());
        }
        Benchmark.report();
    }
}
