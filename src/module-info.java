module eu.menzani.ringbuffer {
    requires static net.bytebuddy;
    requires static jdk.unsupported;
    requires static jdk.management;

    exports eu.menzani.ringbuffer.classcopy;
    exports eu.menzani.ringbuffer.concurrent;
    exports eu.menzani.ringbuffer.java;
    exports eu.menzani.ringbuffer.marshalling;
    exports eu.menzani.ringbuffer.memory;
    exports eu.menzani.ringbuffer.object;
    exports eu.menzani.ringbuffer.system;
    exports eu.menzani.ringbuffer.wait;
}