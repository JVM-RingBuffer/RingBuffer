package eu.menzani.ringbuffer.builder;

enum RingBufferConcurrency {
    VOLATILE,
    ATOMIC_READ,
    ATOMIC_WRITE,
    CONCURRENT
}
