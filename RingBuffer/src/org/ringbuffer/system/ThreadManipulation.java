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

import org.ringbuffer.lang.Assume;
import org.ringbuffer.lang.ImmutableObjectException;

public class ThreadManipulation implements Runnable {
    private static final ThreadManipulation doNothing = new ThreadManipulation().new Frozen();
    private static final ThreadManipulation onlySetPriorityToRealtime = new ThreadManipulation().setPriorityToRealtime().new Frozen();

    private final int cpuToBindTo;
    private boolean shouldSetPriorityToRealtime;

    public static ThreadManipulation doNothing() {
        return doNothing;
    }

    public static ThreadManipulation onlySetPriorityToRealtime() {
        return onlySetPriorityToRealtime;
    }

    public static ThreadManipulation bindToCPU(int cpu) {
        Assume.notNegative(cpu);
        return new ThreadManipulation(cpu);
    }

    public static ThreadManipulation bindFromSpreader(ThreadSpreader spreader) {
        return new ThreadManipulation(spreader.nextCPU());
    }

    private ThreadManipulation(int cpuToBindTo) {
        this.cpuToBindTo = cpuToBindTo;
    }

    ThreadManipulation() {
        cpuToBindTo = -1;
    }

    public ThreadManipulation setPriorityToRealtime() {
        shouldSetPriorityToRealtime = true;
        return this;
    }

    @Override
    public void run() {
        if (cpuToBindTo != -1) {
            Threads.bindCurrentThreadToCPU(cpuToBindTo);
        }
        if (shouldSetPriorityToRealtime) {
            Threads.setCurrentThreadPriorityToRealtime();
        }
    }

    private class Frozen extends ThreadManipulation {
        Frozen() {
        }

        @Override
        public ThreadManipulation setPriorityToRealtime() {
            throw new ImmutableObjectException();
        }

        @Override
        public void run() {
            ThreadManipulation.this.run();
        }
    }
}
