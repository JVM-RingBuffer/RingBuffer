package eu.menzani.ringbuffer.java;

public class Assume {
    public static void notLesser(int value, int cap, String variableName) {
        if (value < cap) {
            throw new IllegalArgumentException(variableName + " must be at least " + cap + ", but is " + value);
        }
    }

    public static void notNegative(int value, String variableName) {
        notLesser(value, 0, variableName);
    }

    public static void notGreater(int value, int cap, String valueVariableName, String capVariableName) {
        if (value > cap) {
            throw new IllegalArgumentException(valueVariableName + " (" + value + ") must not be greater than " + capVariableName + " (" + cap + ')');
        }
    }
}
