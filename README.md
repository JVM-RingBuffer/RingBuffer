```java
EmptyRingBuffer<Integer> producersToProcessor = EmptyRingBuffer.<Integer>withCapacity(5)
        .manyWriters()
        .oneReader()
        .blocking()
        .withGC()
        .build();
PrefilledRingBuffer<Event> processorToConsumers = PrefilledRingBuffer.withCapacityAndFiller(300 + 1, Event::new)
        .oneWriter()
        .manyReaders()
        .waitingWith(YieldBusyWaitStrategy.getDefault())
        .build();
Threads.loadNativeLibrary();

Runnable producer = () -> {
    for (int i = 0; i < 100; i++) {
        producersToProcessor.put(i);
    }
};
Runnable processor = () -> {
    Threads.bindCurrentThreadToCPU(5);
    Threads.setCurrentThreadPriorityToRealtime();

    for (int i = 0; i < 300; i++) {
        Event event = processorToConsumers.next();
        event.setData(producersToProcessor.take());
        processorToConsumers.put();
    }
};
Runnable consumer = () -> {
    for (int i = 0; i < 100 / 5; i++) {
        processorToConsumers.takeBatch(5);
        for (int j = 0; j < 5; j++) {
            System.out.println(processorToConsumers.takePlain().getData());
        }
        processorToConsumers.advanceBatch();
    }
};

for (int i = 0; i < 3; i++) {
    new Thread(producer).start();
    new Thread(consumer).start();
}
new Thread(processor).start();
```