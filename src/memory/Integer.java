package eu.menzani.ringbuffer.memory;

public interface Integer {
    void set(int value);

    int getAndDecrement();

    int get();

    int getPlain();
}
