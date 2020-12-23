package org.ringbuffer.clock;

class ParkedThread {
    Thread thread;
    long endTime;

    boolean shouldTerminate;
}
