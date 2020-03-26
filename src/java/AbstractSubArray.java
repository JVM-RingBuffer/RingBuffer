package eu.menzani.ringbuffer.java;

public interface AbstractSubArray<T> extends AbstractArray<T> {
    AbstractSubArray<T> unmodifiableView();

    int getBeginIndex();

    int getEndIndex();
}
