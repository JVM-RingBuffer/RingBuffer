package eu.menzani.ringbuffer.java;

class IllegalLongStateException extends IllegalStateException {
    IllegalLongStateException(long value) {
        super(Long.toString(value));
    }

    IllegalLongStateException(long value, String operator, long cap) {
        super(value + operator + cap);
    }
}
