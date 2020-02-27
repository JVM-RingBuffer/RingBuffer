```java
RingBuffer<Event> producersToProcessor = RingBuffer.prefilled(301, Event::new)
        .manyWriters()
        .oneReader()
        .build();
RingBuffer<Integer> processorToConsumers = RingBuffer.<Integer>empty(5)
        .oneWriter()
        .manyReaders()
        .blocking()
        .waitingWith(YieldBusyWaitStrategy.getDefault())
        .withGC()
        .build();
ThreadBind.loadNativeLibrary();

Runnable producer = () -> {
    for (int i = 0; i < 100; i++) {
        synchronized (producersToProcessor) {
            Event event = producersToProcessor.next();
            event.setData(i);
            producersToProcessor.put();
        }
    }
};
Runnable processor = () -> {
    ThreadBind.bindCurrentThreadToCPU(5);
    for (int i = 0; i < 300; i++) {
        processorToConsumers.put(producersToProcessor.take().getData());
    }
};
Runnable consumer = () -> {
    for (int i = 0; i < 100; i++) {
        System.out.println(processorToConsumers.take());
    }
};

for (int i = 0; i < 3; i++) {
    new Thread(producer).start();
    new Thread(consumer).start();
}
new Thread(processor).start();
```