package org.ringbuffer.wait;

public class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public ManualMultiStepTest() {
        super(new test.wait.ManualMultiStepTest(false));
    }
}
