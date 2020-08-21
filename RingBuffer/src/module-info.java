module org.ringbuffer {
    requires jdk.unsupported;

    requires static net.bytebuddy;
    requires static jdk.management;

    exports org.ringbuffer.classcopy;
    exports org.ringbuffer.concurrent;
    exports org.ringbuffer.lang;
    exports org.ringbuffer.marshalling;
    exports org.ringbuffer.object;
    exports org.ringbuffer.system;
    exports org.ringbuffer.wait;
}