package eu.menzani.ringbuffer.java;

public interface SubArrayIterator<T> extends ArrayIterator<T> {
    int nextAbsoluteIndex();

    int previousAbsoluteIndex();
}
