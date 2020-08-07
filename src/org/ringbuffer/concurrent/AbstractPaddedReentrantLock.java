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

/*
 * This file is available under and governed by the Apache License
 * version 2.0 only, as published by the Apache Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package org.ringbuffer.concurrent;

import jdk.internal.vm.annotation.ReservedStackAccess;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Base of synchronization control for this lock. Subclassed
 * into fair and nonfair versions below. Uses AQS state to
 * represent the number of holds on the lock.
 */
abstract class AbstractPaddedReentrantLock extends PaddedAbstractQueuedSynchronizer implements Lock, org.ringbuffer.lock.Lock {
    private static final long serialVersionUID = -5179523762034025860L;

    /**
     * Acquires the lock.
     *
     * <p>Acquires the lock if it is not held by another thread and returns
     * immediately, setting the lock hold count to one.
     *
     * <p>If the current thread already holds the lock then the hold
     * count is incremented by one and the method returns immediately.
     *
     * <p>If the lock is held by another thread then the
     * current thread becomes disabled for thread scheduling
     * purposes and lies dormant until the lock has been acquired,
     * at which time the lock hold count is set to one.
     */
    public void lock() {
        lock0();
    }

    /**
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     *
     * <p>Acquires the lock if it is not held by another thread and returns
     * immediately, setting the lock hold count to one.
     *
     * <p>If the current thread already holds this lock then the hold count
     * is incremented by one and the method returns immediately.
     *
     * <p>If the lock is held by another thread then the
     * current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of two things happens:
     *
     * <ul>
     *
     * <li>The lock is acquired by the current thread; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread.
     *
     * </ul>
     *
     * <p>If the lock is acquired by the current thread then the lock hold
     * count is set to one.
     *
     * <p>If the current thread:
     *
     * <ul>
     *
     * <li>has its interrupted status set on entry to this method; or
     *
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
     * the lock,
     *
     * </ul>
     * <p>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>In this implementation, as this method is an explicit
     * interruption point, preference is given to responding to the
     * interrupt over normal or reentrant acquisition of the lock.
     *
     * @throws InterruptedException if the current thread is interrupted
     */
    public void lockInterruptibly() throws InterruptedException {
        lockInterruptibly0();
    }

    /**
     * Acquires the lock only if it is not held by another thread at the time
     * of invocation.
     *
     * <p>Acquires the lock if it is not held by another thread and
     * returns immediately with the value {@code true}, setting the
     * lock hold count to one. Even when this lock has been set to use a
     * fair ordering policy, a call to {@code tryLock()} <em>will</em>
     * immediately acquire the lock if it is available, whether or not
     * other threads are currently waiting for the lock.
     * This &quot;barging&quot; behavior can be useful in certain
     * circumstances, even though it breaks fairness. If you want to honor
     * the fairness setting for this lock, then use
     * {@link #tryLock(long, TimeUnit) tryLock(0, TimeUnit.SECONDS)}
     * which is almost equivalent (it also detects interruption).
     *
     * <p>If the current thread already holds this lock then the hold
     * count is incremented by one and the method returns {@code true}.
     *
     * <p>If the lock is held by another thread then this method will return
     * immediately with the value {@code false}.
     *
     * @return {@code true} if the lock was free and was acquired by the
     * current thread, or the lock was already held by the current
     * thread; and {@code false} otherwise
     */
    public boolean tryLock() {
        return tryLock0();
    }

    /**
     * Acquires the lock if it is not held by another thread within the given
     * waiting time and the current thread has not been
     * {@linkplain Thread#interrupt interrupted}.
     *
     * <p>Acquires the lock if it is not held by another thread and returns
     * immediately with the value {@code true}, setting the lock hold count
     * to one. If this lock has been set to use a fair ordering policy then
     * an available lock <em>will not</em> be acquired if any other threads
     * are waiting for the lock. This is in contrast to the {@link #tryLock()}
     * method. If you want a timed {@code tryLock} that does permit barging on
     * a fair lock then combine the timed and un-timed forms together:
     *
     * <pre> {@code
     * if (lock.tryLock() ||
     *     lock.tryLock(timeout, unit)) {
     *   ...
     * }}</pre>
     *
     * <p>If the current thread
     * already holds this lock then the hold count is incremented by one and
     * the method returns {@code true}.
     *
     * <p>If the lock is held by another thread then the
     * current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     *
     * <ul>
     *
     * <li>The lock is acquired by the current thread; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The specified waiting time elapses
     *
     * </ul>
     *
     * <p>If the lock is acquired then the value {@code true} is returned and
     * the lock hold count is set to one.
     *
     * <p>If the current thread:
     *
     * <ul>
     *
     * <li>has its interrupted status set on entry to this method; or
     *
     * <li>is {@linkplain Thread#interrupt interrupted} while
     * acquiring the lock,
     *
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.  If the time is less than or equal to zero, the method
     * will not wait at all.
     *
     * <p>In this implementation, as this method is an explicit
     * interruption point, preference is given to responding to the
     * interrupt over normal or reentrant acquisition of the lock, and
     * over reporting the elapse of the waiting time.
     *
     * @param timeout the time to wait for the lock
     * @param unit    the time unit of the timeout argument
     * @return {@code true} if the lock was free and was acquired by the
     * current thread, or the lock was already held by the current
     * thread; and {@code false} if the waiting time elapsed before
     * the lock could be acquired
     * @throws InterruptedException if the current thread is interrupted
     * @throws NullPointerException if the time unit is null
     */
    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return tryLockNanos(unit.toNanos(timeout));
    }

    /**
     * Attempts to release this lock.
     *
     * <p>If the current thread is the holder of this lock then the hold
     * count is decremented.  If the hold count is now zero then the lock
     * is released.  If the current thread is not the holder of this
     * lock then {@link IllegalMonitorStateException} is thrown.
     *
     * @throws IllegalMonitorStateException if the current thread does not
     *                                      hold this lock
     */
    public void unlock() {
        release(1);
    }

    /**
     * Returns a {@link Condition} instance for use with this
     * {@link Lock} instance.
     *
     * <p>The returned {@link Condition} instance supports the same
     * usages as do the {@link Object} monitor methods ({@link
     * Object#wait() wait}, {@link Object#notify notify}, and {@link
     * Object#notifyAll notifyAll}) when used with the built-in
     * monitor lock.
     *
     * <ul>
     *
     * <li>If this lock is not held when any of the {@link Condition}
     * {@linkplain Condition#await() waiting} or {@linkplain
     * Condition#signal signalling} methods are called, then an {@link
     * IllegalMonitorStateException} is thrown.
     *
     * <li>When the condition {@linkplain Condition#await() waiting}
     * methods are called the lock is released and, before they
     * return, the lock is reacquired and the lock hold count restored
     * to what it was when the method was called.
     *
     * <li>If a thread is {@linkplain Thread#interrupt interrupted}
     * while waiting then the wait will terminate, an {@link
     * InterruptedException} will be thrown, and the thread's
     * interrupted status will be cleared.
     *
     * <li>Waiting threads are signalled in FIFO order.
     *
     * <li>The ordering of lock reacquisition for threads returning
     * from waiting methods is the same as for threads initially
     * acquiring the lock, which is in the default case not specified,
     * but for <em>fair</em> locks favors those threads that have been
     * waiting the longest.
     *
     * </ul>
     *
     * @return the Condition object
     */
    public Condition newCondition() {
        return new ConditionObject();
    }

    /**
     * Queries the number of holds on this lock by the current thread.
     *
     * <p>A thread has a hold on a lock for each lock action that is not
     * matched by an unlock action.
     *
     * <p>The hold count information is typically only used for testing and
     * debugging purposes. For example, if a certain section of code should
     * not be entered with the lock already held then we can assert that
     * fact:
     *
     * <pre> {@code
     * class X {
     *   PaddedReentrantLock lock = new PaddedReentrantLock();
     *   // ...
     *   public void m() {
     *     assert lock.getHoldCount() == 0;
     *     lock.lock();
     *     try {
     *       // ... method body
     *     } finally {
     *       lock.unlock();
     *     }
     *   }
     * }}</pre>
     *
     * @return the number of holds on this lock by the current thread,
     * or zero if this lock is not held by the current thread
     */
    public int getHoldCount() {
        return isHeldExclusively() ? getState() : 0;
    }

    /**
     * Queries if this lock is held by the current thread.
     *
     * <p>Analogous to the {@link Thread#holdsLock(Object)} method for
     * built-in monitor locks, this method is typically used for
     * debugging and testing. For example, a method that should only be
     * called while a lock is held can assert that this is the case:
     *
     * <pre> {@code
     * class X {
     *   PaddedReentrantLock lock = new PaddedReentrantLock();
     *   // ...
     *
     *   public void m() {
     *       assert lock.isHeldByCurrentThread();
     *       // ... method body
     *   }
     * }}</pre>
     *
     * <p>It can also be used to ensure that a reentrant lock is used
     * in a non-reentrant manner, for example:
     *
     * <pre> {@code
     * class X {
     *   PaddedReentrantLock lock = new PaddedReentrantLock();
     *   // ...
     *
     *   public void m() {
     *       assert !lock.isHeldByCurrentThread();
     *       lock.lock();
     *       try {
     *           // ... method body
     *       } finally {
     *           lock.unlock();
     *       }
     *   }
     * }}</pre>
     *
     * @return {@code true} if current thread holds this lock and
     * {@code false} otherwise
     */
    public boolean isHeldByCurrentThread() {
        return isHeldExclusively();
    }

    /**
     * Queries if this lock is held by any thread. This method is
     * designed for use in monitoring of the system state,
     * not for synchronization control.
     *
     * @return {@code true} if any thread holds this lock and
     * {@code false} otherwise
     */
    public boolean isLocked() {
        return getState() != 0;
    }

    /**
     * Returns the thread that currently owns this lock, or
     * {@code null} if not owned. When this method is called by a
     * thread that is not the owner, the return value reflects a
     * best-effort approximation of current lock status. For example,
     * the owner may be momentarily {@code null} even if there are
     * threads trying to acquire the lock but have not yet done so.
     * This method is designed to facilitate construction of
     * subclasses that provide more extensive lock monitoring
     * facilities.
     *
     * @return the owner, or {@code null} if not owned
     */
    public Thread getOwner() {
        return getState() == 0 ? null : getExclusiveOwnerThread();
    }

    /**
     * Queries whether any threads are waiting to acquire this lock. Note that
     * because cancellations may occur at any time, a {@code true}
     * return does not guarantee that any other thread will ever
     * acquire this lock.  This method is designed primarily for use in
     * monitoring of the system state.
     *
     * @return {@code true} if there may be other threads waiting to
     * acquire the lock
     */
    public final boolean hasQueuedThreads() {
        return hasQueuedThreads0();
    }

    /**
     * Queries whether the given thread is waiting to acquire this
     * lock. Note that because cancellations may occur at any time, a
     * {@code true} return does not guarantee that this thread
     * will ever acquire this lock.  This method is designed primarily for use
     * in monitoring of the system state.
     *
     * @param thread the thread
     * @return {@code true} if the given thread is queued waiting for this lock
     * @throws NullPointerException if the thread is null
     */
    public final boolean hasQueuedThread(Thread thread) {
        return isQueued(thread);
    }

    /**
     * Returns an estimate of the number of threads waiting to acquire
     * this lock.  The value is only an estimate because the number of
     * threads may change dynamically while this method traverses
     * internal data structures.  This method is designed for use in
     * monitoring system state, not for synchronization control.
     *
     * @return the estimated number of threads waiting for this lock
     */
    public final int getQueueLength() {
        return getQueueLength0();
    }

    /**
     * Returns a collection containing threads that may be waiting to
     * acquire this lock.  Because the actual set of threads may change
     * dynamically while constructing this result, the returned
     * collection is only a best-effort estimate.  The elements of the
     * returned collection are in no particular order.  This method is
     * designed to facilitate construction of subclasses that provide
     * more extensive monitoring facilities.
     *
     * @return the collection of threads
     */
    public Collection<Thread> getQueuedThreads() {
        return getQueuedThreads0();
    }

    /**
     * Queries whether any threads are waiting on the given condition
     * associated with this lock. Note that because timeouts and
     * interrupts may occur at any time, a {@code true} return does
     * not guarantee that a future {@code signal} will awaken any
     * threads.  This method is designed primarily for use in
     * monitoring of the system state.
     *
     * @param condition the condition
     * @return {@code true} if there are any waiting threads
     * @throws IllegalMonitorStateException if this lock is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this lock
     * @throws NullPointerException         if the condition is null
     */
    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof PaddedAbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return hasWaiters((PaddedAbstractQueuedSynchronizer.ConditionObject) condition);
    }

    /**
     * Returns an estimate of the number of threads waiting on the
     * given condition associated with this lock. Note that because
     * timeouts and interrupts may occur at any time, the estimate
     * serves only as an upper bound on the actual number of waiters.
     * This method is designed for use in monitoring of the system
     * state, not for synchronization control.
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     * @throws IllegalMonitorStateException if this lock is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this lock
     * @throws NullPointerException         if the condition is null
     */
    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof PaddedAbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return getWaitQueueLength((PaddedAbstractQueuedSynchronizer.ConditionObject) condition);
    }

    /**
     * Returns a collection containing those threads that may be
     * waiting on the given condition associated with this lock.
     * Because the actual set of threads may change dynamically while
     * constructing this result, the returned collection is only a
     * best-effort estimate. The elements of the returned collection
     * are in no particular order.  This method is designed to
     * facilitate construction of subclasses that provide more
     * extensive condition monitoring facilities.
     *
     * @param condition the condition
     * @return the collection of threads
     * @throws IllegalMonitorStateException if this lock is not held
     * @throws IllegalArgumentException     if the given condition is
     *                                      not associated with this lock
     * @throws NullPointerException         if the condition is null
     */
    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof PaddedAbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return getWaitingThreads((PaddedAbstractQueuedSynchronizer.ConditionObject) condition);
    }

    /**
     * Returns a string identifying this lock, as well as its lock state.
     * The state, in brackets, includes either the String {@code "Unlocked"}
     * or the String {@code "Locked by"} followed by the
     * {@linkplain Thread#getName name} of the owning thread.
     *
     * @return a string identifying this lock, as well as its lock state
     */
    public String toString() {
        Thread o = getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }

    /**
     * Performs non-fair tryLock.
     */
    @ReservedStackAccess
    final boolean tryLock0() {
        Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        } else if (getExclusiveOwnerThread() == current) {
            if (++c < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(c);
            return true;
        }
        return false;
    }

    /**
     * Checks for reentrancy and acquires if lock immediately
     * available under fair vs nonfair rules. Locking methods
     * perform initialTryLock check before relaying to
     * corresponding AQS acquire methods.
     */
    abstract boolean initialTryLock();

    @ReservedStackAccess
    final void lock0() {
        if (!initialTryLock())
            acquire(1);
    }

    @ReservedStackAccess
    final void lockInterruptibly0() throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!initialTryLock())
            acquireInterruptibly(1);
    }

    @ReservedStackAccess
    final boolean tryLockNanos(long nanos) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return initialTryLock() || tryAcquireNanos(1, nanos);
    }

    @ReservedStackAccess
    protected final boolean tryRelease(int releases) {
        int c = getState() - releases;
        if (getExclusiveOwnerThread() != Thread.currentThread())
            throw new IllegalMonitorStateException();
        boolean free = (c == 0);
        if (free)
            setExclusiveOwnerThread(null);
        setState(c);
        return free;
    }

    protected final boolean isHeldExclusively() {
        // While we must in general read state before owner,
        // we don't need to do so to check if current thread is owner
        return getExclusiveOwnerThread() == Thread.currentThread();
    }

    /**
     * Reconstitutes the instance from a stream (that is, deserializes it).
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        setState(0); // reset to unlocked state
    }
}
