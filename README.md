```java
RingBuffer<Integer> producersToProcessor = RingBuffer.<Integer>empty(5)
        .manyWriters()
        .oneReader()
        .blocking()
        .withGC()
        .build();
RingBuffer<Event> processorToConsumers = RingBuffer.prefilled(301, Event::new)
        .oneWriter()
        .manyReaders()
        .waitingWith(YieldBusyWaitStrategy.getDefault())
        .build();
ThreadBind.loadNativeLibrary();

Runnable producer = () -> {
    for (int i = 0; i < 100; i++) {
        producersToProcessor.put(i);
    }
};
Runnable processor = () -> {
    ThreadBind.bindCurrentThreadToCPU(5);
    for (int i = 0; i < 300; i++) {
        Event event = processorToConsumers.next();
        event.setData(producersToProcessor.take());
        processorToConsumers.put();
    }
};
Runnable consumer = () -> {
    Event[] buffer = new Event[5];
    for (int i = 0; i < 100 / 5; i++) {
        processorToConsumers.fill(buffer);
        for (Event event : buffer) {
            System.out.println(event.getData());
        }
    }
};

for (int i = 0; i < 3; i++) {
    new Thread(producer).start();
    new Thread(consumer).start();
}
new Thread(processor).start();
```