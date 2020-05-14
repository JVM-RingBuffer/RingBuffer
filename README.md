```java
private static final EmptyRingBuffer<Integer> producersToProcessor =
        EmptyRingBuffer.<Integer>withCapacity(5)
                .manyWriters()
                .oneReader()
                .blocking()
                .withGC()
                .build();
private static final OverwritingPrefilledRingBuffer<Event> processorToConsumers =
        PrefilledRingBuffer.withCapacityAndFiller(300 + 1, new Filler())
                .oneWriter()
                .manyReaders()
                .waitingWith(YieldBusyWaitStrategy.getDefault())
                .build();

static class Filler implements Supplier<Event> {
    @Override
    public Event get() {
        return new Event();
    }
}

public static void main(String[] args) {
    Threads.loadNativeLibrary();

    for (int i = 0; i < 3; i++) {
        new Producer().start();
        new Consumer().start();
    }
    new Processor().start();
}

static class Producer extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            producersToProcessor.put(i);
        }
    }
}

static class Processor extends Thread {
    @Override
    public void run() {
        Threads.bindCurrentThreadToCPU(5);
        Threads.setCurrentThreadPriorityToRealtime();

        for (int i = 0; i < 300; i++) {
            Integer data = producersToProcessor.take();
            int key = processorToConsumers.nextKey();
            Event event = processorToConsumers.next(key);
            event.setData(data);
            processorToConsumers.put(key);
        }
    }
}

static class Consumer extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100 / 5; i++) {
            processorToConsumers.takeBatch(5);
            for (int j = 0; j < 5; j++) {
                System.out.println(processorToConsumers.takePlain().getData());
            }
            processorToConsumers.advanceBatch();
        }
    }
}

static class Event {
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
```