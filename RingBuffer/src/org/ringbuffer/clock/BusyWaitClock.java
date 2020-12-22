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

package org.ringbuffer.clock;

import eu.menzani.atomic.AtomicLong;
import eu.menzani.lang.Lang;
import eu.menzani.lang.StaticFieldOffset;
import eu.menzani.system.ThreadManipulation;
import eu.menzani.system.Unsafe;
import eu.menzani.util.SimpleObjectToggle;

public class BusyWaitClock {
    static final Object TIME_base;
    static final long TIME_offset;

    private static final SimpleObjectToggle<Thread> worker = new SimpleObjectToggle<>();

    static {
        StaticFieldOffset TIME = Lang.staticFieldOffset(BusyWaitClock.class, "time");
        TIME_base = TIME.getBase();
        TIME_offset = TIME.getOffset();
    }

    private static long time;

    public static void start() {
        start(ThreadManipulation.doNothing());
    }

    public static void start(ThreadManipulation threadManipulation) {
        synchronized (worker) {
            worker.ensureNotSet("Clock is already running.");
            Thread worker = new Worker(threadManipulation);
            worker.setDaemon(true);
            worker.start();
            BusyWaitClock.worker.set(worker);
        }
    }

    public static void stop() throws InterruptedException {
        synchronized (worker) {
            Thread worker = BusyWaitClock.worker.ensureSet("Clock is not running.");
            Worker.terminate();
            worker.join();
        }
    }

    public static long currentTime() {
        return AtomicLong.getOpaque(TIME_base, TIME_offset);
    }

    public static void sleepCurrentThread(int nanoseconds) {
        Worker.sleep(Thread.currentThread(), currentTime() + nanoseconds);
        Unsafe.park(0L);
    }
}
