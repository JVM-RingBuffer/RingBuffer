package org.ringbuffer.wait;

import bench.wait.LinkedMultiStepBenchmark;

public class LinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public LinkedMultiStepTest() {
        super(new LinkedMultiStepBenchmark(false));
    }
}
