package eu.menzani.ringbuffer.java;

public interface SubArray<T> extends Array<T> {
    SubArray<T> unmodifiableView();

    int getBeginIndex();

    int getEndIndex();
}
