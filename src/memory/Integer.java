package eu.menzani.ringbuffer.memory;

public interface Integer {
    void set(int value);

    int getAndDecrement();

    int decrementAndGetPlain();

    int getPlainAndIncrement();

    int get();

    int getPlain();
}
