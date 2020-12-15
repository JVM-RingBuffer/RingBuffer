/*
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

package org.ringbuffer.system;

public class ThreadManipulation {
    private static final ThreadManipulation doNothing = new ThreadManipulation(-1, false);
    private static final ThreadManipulation setPriorityToRealtime = new ThreadManipulation(-1, true);

    private final int cpuToBindTo;
    private final boolean shouldSetPriorityToRealtime;

    public static ThreadManipulation doNothing() {
        return doNothing;
    }

    public static ThreadManipulation setPriorityToRealtime() {
        return setPriorityToRealtime;
    }

    public static ThreadManipulation bindToCPU(int cpu) {
        Threads.validateCPU(cpu);
        return new ThreadManipulation(cpu, false);
    }

    public static ThreadManipulation bindFromSpreader(ThreadSpreader spreader) {
        return new ThreadManipulation(spreader.nextCPU(), false);
    }

    public static ThreadManipulation setPriorityToRealtimeAndBindToCPU(int cpu) {
        Threads.validateCPU(cpu);
        return new ThreadManipulation(cpu, true);
    }

    public static ThreadManipulation setPriorityToRealtimeAndBindFromSpreader(ThreadSpreader spreader) {
        return new ThreadManipulation(spreader.nextCPU(), true);
    }

    private ThreadManipulation(int cpuToBindTo, boolean shouldSetPriorityToRealtime) {
        this.cpuToBindTo = cpuToBindTo;
        this.shouldSetPriorityToRealtime = shouldSetPriorityToRealtime;
    }

    public void applyToCurrentThread() {
        if (cpuToBindTo != -1) {
            Threads.bindCurrentThreadToCPU(cpuToBindTo);
        }
        if (shouldSetPriorityToRealtime) {
            Threads.setCurrentThreadPriorityToRealtime();
        }
    }
}
