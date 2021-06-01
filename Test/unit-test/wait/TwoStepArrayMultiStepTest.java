package org.ringbuffer.wait;

import bench.wait.TwoStepArrayMultiStepBenchmark;

public class TwoStepArrayMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public TwoStepArrayMultiStepTest() {
        super(new TwoStepArrayMultiStepBenchmark(false));
    }
}
