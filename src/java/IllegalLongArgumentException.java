package org.ringbuffer.java;

class IllegalLongArgumentException extends IllegalArgumentException {
    IllegalLongArgumentException(long value) {
        super(Long.toString(value));
    }

    IllegalLongArgumentException(long value, String operator, long cap) {
        super(value + operator + cap);
    }
}
