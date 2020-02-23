package eu.menzani.ringbuffer;

class PrefilledSynchronizedWriter extends PrefilledWriter {
    PrefilledSynchronizedWriter(int numIterations) {
        super(numIterations);
    }

    @Override
    void write(int i) {
        synchronized (Test.ringBuffer) {
            super.write(i);
        }
    }
}
