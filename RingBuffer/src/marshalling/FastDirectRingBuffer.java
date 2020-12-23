package org.ringbuffer.marshalling;

abstract class FastDirectRingBuffer implements DirectRingBuffer {
    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void advance(long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }
}
