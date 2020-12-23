package org.ringbuffer.marshalling;

import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

abstract class MarshallingRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
    MarshallingRingBufferBuilder() {
    }

    MarshallingRingBufferBuilder(MarshallingRingBufferBuilder<?> builder) {
        super(builder);
    }

    @Override
    protected BusyWaitStrategy getWriteBusyWaitStrategy() {
        return super.getWriteBusyWaitStrategy();
    }

    @Override
    protected BusyWaitStrategy getReadBusyWaitStrategy() {
        return super.getReadBusyWaitStrategy();
    }
}
