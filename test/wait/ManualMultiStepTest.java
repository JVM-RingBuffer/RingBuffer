package eu.menzani.ringbuffer.wait;

class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    ManualMultiStepTest() {
        super(new perftest.wait.ManualMultiStepTest());
    }
}
