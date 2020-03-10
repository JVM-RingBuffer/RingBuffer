package eu.menzani.ringbuffer.java;

import java.util.Arrays;
import java.util.Collections;

class ArrayTest extends AbstractArrayTest {
    @Override
    AbstractArray<String> getArray() {
        return Array.of(null, ONE, TWO, THREE, FOUR);
    }

    @Override
    AbstractArray<String> getArrayFromCollection() {
        return new Array<>(Arrays.asList(null, ONE, TWO, THREE, FOUR));
    }

    @Override
    AbstractArray<String> getArrayWithRepeatedElements() {
        return Array.of(ELEMENTS);
    }

    @Override
    AbstractArray<String> getEmptyArray() {
        return Array.empty(5);
    }

    @Override
    AbstractArray<String> getEmptyArrayFromCollection() {
        return new Array<>(Collections.nCopies(5, null));
    }
}
