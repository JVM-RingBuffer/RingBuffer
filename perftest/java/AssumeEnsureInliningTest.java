package test.java;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Ensure;

class AssumeEnsureInliningTest {
    private static final int numIterations = 100_000;
    private static final long numIterationsAsLong = 100_000L;

    public static void main(String[] args) {
        for (int i = 0; i < numIterations; i++) {
            Assume.lesser(i, numIterations);
            Assume.notLesser(i, -5);
            Assume.notGreater(i, numIterations - 1);
            Assume.notNegative(i);
            Assume.notZero(i);

            Ensure.notGreater(i, numIterations - 1);
            Ensure.notZero(i);
        }
        for (long i = 0L; i < numIterationsAsLong; i++) {
            Assume.notNegative(i);
        }
    }
}
