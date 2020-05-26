package eu.menzani.ringbuffer.java;

class IllegalIntArgumentException extends IllegalArgumentException {
    IllegalIntArgumentException(int value) {
        super(Integer.toString(value));
    }

    IllegalIntArgumentException(int value, String operator, int cap) {
        super(value + operator + cap);
    }
}
