package perftest;

import eu.menzani.ringbuffer.java.Assume;

class AssumeInliningTest {
    private static final int numIterations = 100_000;

    public static void main(String[] args) {
        int lesserCap = Integer.parseInt("-5");
        int greaterCap = Integer.parseInt(Integer.toString(numIterations - 1));

        for (int i = 0; i < numIterations; i++) {
            Assume.notLesser(i, lesserCap);
            Assume.notGreater(i, greaterCap);
            Assume.notNegative(i);
        }
    }
}
