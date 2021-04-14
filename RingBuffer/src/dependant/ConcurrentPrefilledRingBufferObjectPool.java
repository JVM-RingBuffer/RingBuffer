package org.ringbuffer.dependant;

import eu.menzani.object.ObjectFactory;
import eu.menzani.object.ObjectPool;
import eu.menzani.object.PoolObject;
import org.ringbuffer.object.RingBuffer;

public class ConcurrentPrefilledRingBufferObjectPool<T extends PoolObject> implements ObjectPool<T> {
    private final RingBuffer<T> ringBuffer;

    public ConcurrentPrefilledRingBufferObjectPool(int capacity, ObjectFactory<T> filler) {
        ringBuffer = RingBuffer.<T>withCapacity(capacity)
                .manyWriters()
                .manyReaders()
                .lockfree()
                .build();

        capacity /= 2;
        for (int i = 0; i < capacity; i++) {
            ringBuffer.put(filler.newInstance());
        }
    }

    @Override
    public T release() {
        return ringBuffer.take();
    }

    @Override
    public void acquire(T object) {
        ringBuffer.put(object);
    }

    @Override
    public boolean isFull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gc() {
        ringBuffer.forEach(PoolObject::gc);
    }
}
