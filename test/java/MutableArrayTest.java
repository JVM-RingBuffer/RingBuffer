package eu.menzani.ringbuffer.java;

import java.util.Arrays;
import java.util.Collections;

class MutableArrayTest extends AbstractMutableArrayTest {
    MutableArrayTest() {
        super(false);
    }

    @Override
    Array<String> getArray() {
        return Array.of(null, ONE, TWO, THREE, FOUR);
    }

    @Override
    Array<String> getArrayFromCollection() {
        return Array.fromCollection(Arrays.asList(null, ONE, TWO, THREE, FOUR));
    }

    @Override
    Array<String> getArrayWithRepeatedElements() {
        return Array.of(ELEMENTS);
    }

    @Override
    Array<String> getEmptyArray() {
        return Array.empty(5);
    }

    @Override
    Array<String> getEmptyArrayFromCollection() {
        return Array.fromCollection(Collections.nCopies(5, null));
    }
}
