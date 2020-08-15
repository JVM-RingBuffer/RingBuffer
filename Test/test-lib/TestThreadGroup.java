/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import org.ringbuffer.java.Nullable;

import java.util.function.IntFunction;

public class TestThreadGroup {
    private final AbstractTestThread[] testThreads;

    public TestThreadGroup(IntFunction<AbstractTestThread> testThreadFactory) {
        testThreads = new AbstractTestThread[AbstractRingBufferTest.CONCURRENCY];
        for (int i = 0; i < testThreads.length; i++) {
            testThreads[i] = testThreadFactory.apply(AbstractRingBufferTest.NUM_ITERATIONS);
        }
    }

    public void start(@Nullable Profiler profiler) {
        for (AbstractTestThread testThread : testThreads) {
            testThread.start();
        }
        for (AbstractTestThread testThread : testThreads) {
            testThread.waitUntilReady();
        }
        for (AbstractTestThread testThread : testThreads) {
            testThread.commenceExecution();
        }
        if (profiler != null) {
            profiler.start();
        }
    }

    public void waitForCompletion(@Nullable Profiler profiler) {
        for (AbstractTestThread testThread : testThreads) {
            testThread.waitForCompletion();
        }
        if (profiler != null) {
            profiler.stop();
        }
    }

    public long getReaderSum() {
        long sum = 0L;
        for (AbstractTestThread testThread : testThreads) {
            sum += ((AbstractReader) testThread).getSum();
        }
        return sum;
    }
}
