package org.ringbuffer.dependant;

import eu.menzani.atomic.AtomicLong;
import eu.menzani.lang.Lang;
import eu.menzani.lang.StaticFieldOffset;
import eu.menzani.struct.SimpleObjectToggle;
import eu.menzani.system.ThreadManipulation;
import eu.menzani.system.Unsafe;

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
