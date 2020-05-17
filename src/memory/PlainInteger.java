package eu.menzani.ringbuffer.memory;

class PlainInteger implements Integer {
    private int value;

    @Override
    public void set(int value) {
        this.value = value;
    }

    @Override
    public int get() {
        return value;
    }

    @Override
    public int getPlain() {
        return value;
    }
}
