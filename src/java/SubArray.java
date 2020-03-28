package eu.menzani.ringbuffer.java;

public interface SubArray<T> extends Array<T> {
    /**
     * It returns the same value as if it was called on the main array.
     */
    @Override
    T[] getElements();

    SubArray<T> unmodifiableView();

    int getBeginIndex();

    int getEndIndex();
}
