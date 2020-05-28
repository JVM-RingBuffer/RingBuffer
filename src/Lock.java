package org.ringbuffer;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Lock {
    private final ReentrantLock lock = new ReentrantLock();

    public void lock() {
        while (!lock.tryLock()) {
            LockSupport.parkNanos(1L);
        }
    }

    public void unlock() {
        lock.unlock();
    }
}
