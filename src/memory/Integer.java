package eu.menzani.ringbuffer.memory;

public interface Integer {
    void set(int value);

    int get();

    int getPlain();

    int getAndDecrement();
}
