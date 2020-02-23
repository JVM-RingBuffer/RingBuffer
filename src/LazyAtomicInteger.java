package eu.menzani.ringbuffer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;

class LazyAtomicInteger {
    private final AtomicInteger value = new AtomicInteger();
    private final AtomicInteger temp = new AtomicInteger();

    // Adapted from AtomicInteger.getAndAccumulate() and AtomicInteger.accumulateAndGet()
    long update(int constant, IntBinaryOperator operator) {
        int previous = get();
        int next = 0;
        boolean retry = false;
        while (true) {
            if (!retry) {
                next = operator.applyAsInt(previous, constant);
            }
            if (temp.weakCompareAndSetVolatile(previous, next)) {
                return IntPair.of(previous, next);
            }
            retry = (previous == (previous = get()));
        }
    }

    void afterUpdate() {
        value.setRelease(temp.get());
    }

    int get() {
        return value.getAcquire();
    }
}
