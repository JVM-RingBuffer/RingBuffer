package org.ringbuffer.dependant;

class ParkedThread {
    Thread thread;
    long endTime;

    boolean shouldTerminate;
}
