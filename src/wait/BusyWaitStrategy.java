package eu.menzani.ringbuffer.wait;

public interface BusyWaitStrategy {
    default void reset() {}

    void tick();

    Factory NOOP = NoopBusyWaitStrategy::getDefault;
    Factory HINT = HintBusyWaitStrategy::getDefault;
    Factory YIELD = YieldBusyWaitStrategy::getDefault;
    Factory SLEEP = SleepBusyWaitStrategy::getDefault;
    Factory FAIL_READING_TOO_SLOW = FailBusyWaitStrategy::readingTooSlow;
    Factory FAIL_WRITING_TOO_SLOW = FailBusyWaitStrategy::writingTooSlow;

    interface Factory {
        BusyWaitStrategy newInstanceOrReusedIfThreadSafe();
    }
}
