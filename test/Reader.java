package eu.menzani.ringbuffer;

class Reader extends Thread {
    private final int numIterations;
    private int sum;

    Reader(int numIterations) {
        this.numIterations = numIterations;
        start();
    }

    int getSum() {
        return sum;
    }

    @Override
    public void run() {
        for (int i = 0; i < numIterations; i++) {
            sum += Test.ringBuffer.take().getData();
        }
    }
}
