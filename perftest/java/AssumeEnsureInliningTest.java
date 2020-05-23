package test.java;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Ensure;

class AssumeEnsureInliningTest {
    private static final int numIterations = 100_000;
    private static final long numIterationsAsLong = 100_000L;
    private static final Object object = new Object();

    public static void main(String[] args) {
        for (int i = 1; i <= numIterations; i++) {
            Assume.lesser(i, numIterations + 1);
            Assume.notLesser(i, -5);
            Assume.notGreater(i, numIterations);
            Assume.notNegative(i);
            Assume.notZero(i);

            Ensure.notGreater(i, numIterations);
            Ensure.notZero(i);
            Ensure.notNull(object);
        }
        for (long i = 1L; i <= numIterationsAsLong; i++) {
            Assume.notLesser(i, -5);
            Assume.notNegative(i);
        }
    }
}
