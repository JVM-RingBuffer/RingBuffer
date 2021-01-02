package org.ringbuffer.marshalling;

abstract class LockfreeHeapRingBuffer implements HeapRingBuffer {
    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void advance(int offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNotEmpty() {
        throw new UnsupportedOperationException();
    }
}
