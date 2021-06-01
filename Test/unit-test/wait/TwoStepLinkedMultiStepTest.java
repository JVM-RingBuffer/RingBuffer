package org.ringbuffer.wait;

import bench.wait.TwoStepLinkedMultiStepBenchmark;

public class TwoStepLinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public TwoStepLinkedMultiStepTest() {
        super(new TwoStepLinkedMultiStepBenchmark(false));
    }
}
