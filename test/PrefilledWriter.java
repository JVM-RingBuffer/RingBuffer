package eu.menzani.ringbuffer;

class PrefilledWriter extends Thread {
    private final int numIterations;

    PrefilledWriter(int numIterations) {
        this.numIterations = numIterations;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < numIterations; i++) {
            write(i);
        }
    }

    void write(int i) {
        Event event = Test.ringBuffer.next();
        event.setData(i);
        Test.ringBuffer.put();
    }
}
