package org.ringbuffer.wait;

import bench.wait.ArrayMultiStepBenchmark;

public class ArrayMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public ArrayMultiStepTest() {
        super(new ArrayMultiStepBenchmark(false));
    }
}
