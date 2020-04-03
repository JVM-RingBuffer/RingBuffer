package eu.menzani.ringbuffer.wait;

public interface BusyWaitStrategy {
    default void reset() {}

    void tick();

    Factory NOOP = new NoopBusyWaitStrategy.Factory();
    Factory HINT = new HintBusyWaitStrategy.Factory();
    Factory YIELD = new YieldBusyWaitStrategy.Factory();
    Factory SLEEP = new SleepBusyWaitStrategy.Factory();
    Factory FAIL_READING_TOO_SLOW = new FailBusyWaitStrategy.ReadingTooSlowFactory();
    Factory FAIL_WRITING_TOO_SLOW = new FailBusyWaitStrategy.WritingTooSlowFactory();

    interface Factory {
        BusyWaitStrategy newInstanceOrReusedIfThreadSafe();
    }
}
