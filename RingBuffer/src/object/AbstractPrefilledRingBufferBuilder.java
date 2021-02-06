package org.ringbuffer.object;

import eu.menzani.lang.Assume;

import java.util.function.Supplier;

abstract class AbstractPrefilledRingBufferBuilder<T> extends ObjectRingBufferBuilder<T> {
    private Supplier<? extends T> filler;
    // All fields are copied in <init>(AbstractPrefilledRingBufferBuilder<T>)

    AbstractPrefilledRingBufferBuilder(int capacity) {
        super(capacity);
    }

    AbstractPrefilledRingBufferBuilder(AbstractPrefilledRingBufferBuilder<T> builder) {
        super(builder);
        filler = builder.filler;
    }

    public abstract AbstractPrefilledRingBufferBuilder<T> fillWith(Supplier<? extends T> filler);

    void fillWith0(Supplier<? extends T> filler) {
        Assume.notNull(filler);
        this.filler = filler;
    }

    @Override
    protected void validate() {
        super.validate();
        if (filler == null) {
            throw new IllegalStateException("You must call fillWith().");
        }
    }

    @Override
    T[] getBuffer() {
        T[] buffer = super.getBuffer();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = filler.get();
        }
        return buffer;
    }

    T getDummyElement() {
        return filler.get();
    }
}