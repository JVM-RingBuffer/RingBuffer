package eu.menzani.ringbuffer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;

class LazyAtomicInteger {
    private int value;
    private final AtomicInteger temp = new AtomicInteger();

    // Adapted from AtomicInteger.getAndAccumulate() and AtomicInteger.accumulateAndGet()
    long update(int constant, IntBinaryOperator operator) {
        int previous = value;
        int next = 0;
        boolean retry = false;
        while (true) {
            if (!retry) {
                next = operator.applyAsInt(previous, constant);
            }
            if (temp.weakCompareAndSetPlain(previous, next)) {
                return IntPair.of(previous, next);
            }
            retry = (previous == (previous = value));
        }
    }

    void afterUpdate() {
        value = temp.getPlain();
    }

    int get() {
        return value;
    }
}
