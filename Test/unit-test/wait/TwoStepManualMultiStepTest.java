package org.ringbuffer.wait;

import bench.wait.TwoStepManualMultiStepBenchmark;

public class TwoStepManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public TwoStepManualMultiStepTest() {
        super(new TwoStepManualMultiStepBenchmark(false));
    }
}
