package eu.menzani.ringbuffer;

class Writer extends Thread {
    private final int numIterations;

    Writer(int numIterations) {
        this.numIterations = numIterations;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < numIterations; i++) {
            Event event = new Event();
            event.setData(i);
            Test.ringBuffer.put(event);
        }
    }
}
