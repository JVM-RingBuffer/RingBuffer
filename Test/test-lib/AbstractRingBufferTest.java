package test;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.benchmark.Profiler;
import eu.menzani.benchmark.ResultFormat;
import eu.menzani.lang.Check;
import eu.menzani.system.ThreadManipulation;

public abstract class AbstractRingBufferTest extends Benchmark {
    protected static final int NUM_ITERATIONS = 1_000_000;
    protected static final int TOTAL_ELEMENTS = NUM_ITERATIONS * Config.concurrentProducersAndConsumers;

    protected static final long ONE_TO_ONE_SUM = getOneToOneSum();
    protected static final long ONE_TO_MANY_SUM = getOneToManySum();
    protected static final long MANY_WRITERS_SUM = ONE_TO_ONE_SUM * Config.concurrentProducersAndConsumers;

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

    private boolean checkSum = true;

    protected void doNotCheckSum() {
        checkSum = false;
    }

    protected abstract long getSum();

    protected abstract long testSum();

    protected static Profiler createThroughputProfiler(int divideBy) {
        return new Profiler("Throughput", divideBy, ResultFormat.THROUGHPUT);
    }

    @Override
    protected ThreadManipulation getThreadManipulation() {
        return ThreadManipulation.doNothing();
    }

    @Override
    protected boolean shouldAutoProfile() {
        return false;
    }

    @Override
    protected void test(int i) {
        AbstractTestThread.resetThreadSpreader();

        long sum = testSum();
        if (checkSum) {
            Check.equalTo(sum, getSum());
        }
    }
}
