package eu.menzani.ringbuffer.java;

public class Assume {
    public static void notLesser(int value, int cap, String variableName) {
        if (value < cap) {
            throw new IllegalArgumentException(notLesser(variableName, value, cap));
        }
    }

    private static String notLesser(String variableName, int value, int cap) {
        return variableName + " must be at least " + cap + ", but is " + value;
    }

    public static void notNegative(int value, String variableName) {
        if (value < 0) {
            throw new IllegalArgumentException(notNegative(variableName, value));
        }
    }

    private static String notNegative(String variableName, int value) {
        return variableName + " must not be negative, but is " + value;
    }

    public static void notGreater(int value, int cap, String valueVariableName, String capVariableName) {
        if (value > cap) {
            throw new IllegalArgumentException(notGreater(valueVariableName, capVariableName, value, cap));
        }
    }

    private static String notGreater(String valueVariableName, String capVariableName, int value, int cap) {
        return valueVariableName + " (" + value + ") must not be greater than " + capVariableName + " (" + cap + ')';
    }
}
