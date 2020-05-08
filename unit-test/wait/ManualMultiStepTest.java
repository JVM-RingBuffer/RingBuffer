package eu.menzani.ringbuffer.wait;

class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    ManualMultiStepTest() {
        super(new test.wait.ManualMultiStepTest(false));
    }
}
