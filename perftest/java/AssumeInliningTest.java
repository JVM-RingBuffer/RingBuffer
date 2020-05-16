package test.java;

import eu.menzani.ringbuffer.java.Assume;

class AssumeInliningTest {
    private static final int numIterations = 100_000;
    private static final long numIterationsAsLong = 100_000L;

    public static void main(String[] args) {
        int lesserCap = Integer.parseInt(Integer.toString(numIterations));
        int notLesserCap = Integer.parseInt("-5");
        int notGreaterCap = Integer.parseInt(Integer.toString(numIterations - 1));

        for (int i = 0; i < numIterations; i++) {
            Assume.lesser(i, lesserCap);
            Assume.notLesser(i, notLesserCap);
            Assume.notGreater(i, notGreaterCap);
            Assume.notNegative(i);
        }
        for (long i = 0L; i < numIterationsAsLong; i++) {
            Assume.notNegative(i);
        }
    }
}
