package test;

import static test.ProducersToProcessorToConsumersTest.*;

class Processor extends TestThread {
    static Processor startAsync() {
        Processor processor = new Processor();
        processor.startNow();
        return processor;
    }

    static void runAsync() {
        startAsync().waitForCompletion();
    }

    private Processor() {
        super(TOTAL_ELEMENTS, null);
    }

    @Override
    String getProfilerName() {
        return "Processor";
    }

    @Override
    void loop() {
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int eventData = PRODUCERS_RING_BUFFER.take().getData();
            int key = CONSUMERS_RING_BUFFER.nextKey();
            CONSUMERS_RING_BUFFER.next(key).setData(eventData);
            CONSUMERS_RING_BUFFER.put(key);
        }
    }
}
