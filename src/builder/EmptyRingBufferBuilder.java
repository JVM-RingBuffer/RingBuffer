package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.object.EmptyRingBuffer;
import eu.menzani.ringbuffer.object.RingBuffer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.object.BuilderProxy.*;

public class EmptyRingBufferBuilder<T> extends RingBufferBuilder<T> {
    private boolean gcEnabled;

    public EmptyRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public EmptyRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> blocking() {
        super.blocking0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> discarding() {
        super.discarding0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    public EmptyRingBufferBuilder<T> withGC() {
        gcEnabled = true;
        return this;
    }

    @Override
    RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(volatileGCRingBuffer());
                            }
                            return volatileGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(volatileRingBuffer());
                        }
                        return volatileRingBuffer(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(volatileBlockingGCRingBuffer());
                            }
                            return volatileBlockingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(volatileBlockingRingBuffer());
                        }
                        return volatileBlockingRingBuffer(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(volatileDiscardingGCRingBuffer());
                            }
                            return volatileDiscardingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(volatileDiscardingRingBuffer());
                        }
                        return volatileDiscardingRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(atomicReadGCRingBuffer());
                            }
                            return atomicReadGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(atomicReadRingBuffer());
                        }
                        return atomicReadRingBuffer(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(atomicReadBlockingGCRingBuffer());
                            }
                            return atomicReadBlockingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(atomicReadBlockingRingBuffer());
                        }
                        return atomicReadBlockingRingBuffer(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(atomicReadDiscardingGCRingBuffer());
                            }
                            return atomicReadDiscardingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(atomicReadDiscardingRingBuffer());
                        }
                        return atomicReadDiscardingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(atomicWriteGCRingBuffer());
                            }
                            return atomicWriteGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(atomicWriteRingBuffer());
                        }
                        return atomicWriteRingBuffer(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(atomicWriteBlockingGCRingBuffer());
                            }
                            return atomicWriteBlockingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(atomicWriteBlockingRingBuffer());
                        }
                        return atomicWriteBlockingRingBuffer(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(atomicWriteDiscardingGCRingBuffer());
                            }
                            return atomicWriteDiscardingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(atomicWriteDiscardingRingBuffer());
                        }
                        return atomicWriteDiscardingRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(concurrentGCRingBuffer());
                            }
                            return concurrentGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(concurrentRingBuffer());
                        }
                        return concurrentRingBuffer(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(concurrentBlockingGCRingBuffer());
                            }
                            return concurrentBlockingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(concurrentBlockingRingBuffer());
                        }
                        return concurrentBlockingRingBuffer(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            if (copyClass) {
                                return instantiateCopy(concurrentDiscardingGCRingBuffer());
                            }
                            return concurrentDiscardingGCRingBuffer(this);
                        }
                        if (copyClass) {
                            return instantiateCopy(concurrentDiscardingRingBuffer());
                        }
                        return concurrentDiscardingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public EmptyRingBuffer<T> build() {
        return (EmptyRingBuffer<T>) super.build();
    }
}
