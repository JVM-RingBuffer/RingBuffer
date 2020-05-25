package eu.menzani.ringbuffer.builder;

abstract class AbstractBaseMarshallingRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
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
}
