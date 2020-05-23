package eu.menzani.ringbuffer.memory;

class PlainLong implements Long {
    private long value;

    @Override
    public void set(long value) {
        this.value = value;
    }

    @Override
    public long get() {
        return value;
    }

    @Override
    public long getPlain() {
        return value;
    }
}
