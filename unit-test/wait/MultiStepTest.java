package org.ringbuffer.wait;

class MultiStepTest extends MultiStepBusyWaitStrategyTest {
    MultiStepTest() {
        super(new test.wait.MultiStepTest(false));
    }
}
