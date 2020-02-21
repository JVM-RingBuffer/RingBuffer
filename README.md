```java
RingBuffer<Event> producersToProcessor = RingBuffer.prefilled(301, Event::new)
        .manyWriters()
        .oneReader()
        .build();
RingBuffer<Integer> processorToConsumers = RingBuffer.<Integer>empty(5)
        .oneWriter()
        .manyReaders()
        .blocking()
        .waitingWith(new YieldBusyWaitStrategy())
        .withGC()
        .build();

Runnable producer = () -> {
    for (int i = 0; i < 100; i++) {
        synchronized (producersToProcessor) {
            Event event = producersToProcessor.put();
            event.setData(i);
            producersToProcessor.commit();
        }
    }
};
Runnable processor = () -> {
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