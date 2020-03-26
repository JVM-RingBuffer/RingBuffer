package eu.menzani.ringbuffer.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MutableSubArrayTest extends AbstractMutableArrayTest {
    MutableSubArrayTest() {
        super(true);
    }

    @Override
    Array<String> getArray() {
        return Array.of(null, ONE, TWO, THREE, FOUR, FIVE, null).subList(0, 5);
    }

    @Override
    Array<String> getArrayFromCollection() {
        return Array.fromCollection(Arrays.asList("-2", "-1", null, ONE, TWO, THREE, FOUR, FIVE, "6")).subList(2, 7);
    }

    @Override
    Array<String> getArrayWithRepeatedElements() {
        List<String> elements = new ArrayList<>();
        elements.add("-2");
        elements.add("-1");
        Collections.addAll(elements, ELEMENTS);
        return Array.of(elements.toArray(new String[0])).subList(2, 11);
    }

    @Override
    Array<String> getEmptyArray() {
        return Array.<String>empty(9).subList(3, 8);
    }

    @Override
    Array<String> getEmptyArrayFromCollection() {
        return Array.<String>fromCollection(Collections.nCopies(5, null)).subList(0, 5);
    }
}
