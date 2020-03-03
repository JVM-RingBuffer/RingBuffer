package eu.menzani.ringbuffer.java;

import eu.menzani.ringbuffer.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntPairTest {
    private static final Random random = Random.INSTANCE;

    @Test
    void packIntegers() {
        int first = random.nextInt();
        int second = random.nextInt();
        long intPair = IntPair.of(first, second);
        assertEquals(first, IntPair.getFirst(intPair));
        assertEquals(second, IntPair.getSecond(intPair));
    }

    @Test
    void splitLong() {
        long intPair = random.nextLong();
        int first = IntPair.getFirst(intPair);
        int second = IntPair.getSecond(intPair);
        assertEquals(intPair, IntPair.of(first, second));
    }
}
