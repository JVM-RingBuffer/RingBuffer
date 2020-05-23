package eu.menzani.ringbuffer.builder;

abstract class MarshallingRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
    boolean unsafe;
    // All fields are copied in <init>(MarshallingRingBufferBuilder<T>)

    MarshallingRingBufferBuilder() {}

    MarshallingRingBufferBuilder(MarshallingRingBufferBuilder<?> builder) {
        super(builder);
        unsafe = builder.unsafe;
    }

    public abstract MarshallingRingBufferBuilder<T> unsafe();

    void unsafe0() {
        unsafe = true;
    }
}
