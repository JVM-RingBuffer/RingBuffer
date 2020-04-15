package eu.menzani.ringbuffer.wait;

public interface BusyWaitStrategy {
    default void reset() {}

    void tick();
}
