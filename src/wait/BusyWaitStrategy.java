package eu.menzani.ringbuffer.wait;

public interface BusyWaitStrategy {
    void reset();

    void tick();
}
