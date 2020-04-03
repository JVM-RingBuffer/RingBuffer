package eu.menzani.ringbuffer.memory;

public interface BooleanArray {
    void setTrue(int index);

    void setFalse(int index);

    void setFalsePlain(int index);

    boolean isFalse(int index);

    boolean isTrue(int index);

    boolean weakCompareFalseAndSetTrue(int index);
}
