package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.AbstractRingBufferBuilder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.lang.invoke.MethodHandles;

abstract class AbstractBaseMarshallingRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
    private static final MethodHandles.Lookup implLookup = MethodHandles.lookup();

    @Override
    protected MethodHandles.Lookup getImplLookup() {
        return implLookup;
    }

    boolean unsafe;
    // All fields are copied in <init>(AbstractBaseMarshallingRingBufferBuilder<T>)

    AbstractBaseMarshallingRingBufferBuilder() {}

    AbstractBaseMarshallingRingBufferBuilder(AbstractBaseMarshallingRingBufferBuilder<?> builder) {
        super(builder);
        unsafe = builder.unsafe;
    }

    public abstract AbstractBaseMarshallingRingBufferBuilder<T> unsafe();

    void unsafe0() {
        unsafe = true;
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
