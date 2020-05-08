package eu.menzani.ringbuffer;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

class Lock {
    private final ReentrantLock lock = new ReentrantLock();

    void lock() {
        while (!lock.tryLock()) {
            LockSupport.parkNanos(1L);
        }
    }

    void unlock() {
        lock.unlock();
    }
}
