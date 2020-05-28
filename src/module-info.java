module org.ringbuffer {
    requires static net.bytebuddy;
    requires static jdk.unsupported;
    requires static jdk.management;

    exports org.ringbuffer.classcopy;
    exports org.ringbuffer.concurrent;
    exports org.ringbuffer.java;
    exports org.ringbuffer.marshalling;
    exports org.ringbuffer.memory;
    exports org.ringbuffer.object;
    exports org.ringbuffer.system;
    exports org.ringbuffer.wait;
}