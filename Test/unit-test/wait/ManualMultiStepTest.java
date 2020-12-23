package org.ringbuffer.wait;

class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    ManualMultiStepTest() {
        super(new test.wait.ManualMultiStepTest(false));
    }
}
