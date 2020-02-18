package eu.menzani.ringbuffer;

public interface BusyWaitStrategy {
    void reset();

    void tick();
}
