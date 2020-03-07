package eu.menzani.ringbuffer.wait;

class MultiStepTest extends MultiStepBusyWaitStrategyTest {
    MultiStepTest() {
        super(new perftest.wait.MultiStepTest());
    }
}
