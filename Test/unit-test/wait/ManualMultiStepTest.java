package org.ringbuffer.wait;

import bench.wait.ManualMultiStepBenchmark;

public class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public ManualMultiStepTest() {
        super(new ManualMultiStepBenchmark(false));
    }
}
