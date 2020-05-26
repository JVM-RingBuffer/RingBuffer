package eu.menzani.ringbuffer.java;

class IllegalIntStateException extends IllegalStateException {
    IllegalIntStateException(int value) {
        super(Integer.toString(value));
    }

    IllegalIntStateException(int value, String operator, int cap) {
        super(value + operator + cap);
    }
}
