```java
RingBuffer<Event> producersToProcessor = new OneReaderManyWritersRingBuffer<>(
        RingBufferOptions.prefilledBuffer(301, Event::new));
RingBuffer<Integer> processorToConsumers = new ManyReadersOneWriterRingBuffer<>(
        RingBufferOptions.emptyBuffer(301).withGC());

Runnable producer = () -> {
    for (int i = 0; i < 100; i++) {
        synchronized (producersToProcessor) {
            Event event = producersToProcessor.put();
            event.data = ...
            producersToProcessor.commit();
        }
    }
};
Runnable processor = () -> {
    for (int i = 0; i < 300; i++) {
        processorToConsumers.put(producersToProcessor.take().data);
    }
};
Runnable consumer = () -> {
    for (int i = 0; i < 100; i++) {
        System.out.println(processorToConsumers.take());
    }
};

for (int i = 0; i < 3; i++) {
    new Thread(producer).start();
}
new Thread(processor).start();
for (int i = 0; i < 3; i++) {
    new Thread(consumer).start();
}
```