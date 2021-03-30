package org.ringbuffer.object;

import eu.menzani.lang.Assume;
import eu.menzani.object.ObjectFactory;
import eu.menzani.struct.Arrays;

abstract class AbstractPrefilledRingBufferBuilder<T> extends ObjectRingBufferBuilder<T> {
    private ObjectFactory<T> filler;
    // All fields are copied in <init>(AbstractPrefilledRingBufferBuilder<T>)

    AbstractPrefilledRingBufferBuilder(int capacity) {
        super(capacity);
    }

    AbstractPrefilledRingBufferBuilder(AbstractPrefilledRingBufferBuilder<T> builder) {
        super(builder);
        filler = builder.filler;
    }

    public abstract AbstractPrefilledRingBufferBuilder<T> fillWith(ObjectFactory<T> filler);

    void fillWith0(ObjectFactory<T> filler) {
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
        Arrays.fill(buffer, filler);
        return buffer;
    }

    T getDummyElement() {
        return filler.newInstance();
    }
}
