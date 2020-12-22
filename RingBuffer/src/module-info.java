module org.ringbuffer {
    requires transitive eu.menzani;

    exports org.ringbuffer.object;
    exports org.ringbuffer.marshalling;
    exports org.ringbuffer.wait;
    exports org.ringbuffer.clock;
}
