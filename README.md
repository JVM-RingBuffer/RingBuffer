### Object ring buffers

```java
EmptyRingBuffer<Integer> producersToProcessor =
        EmptyRingBuffer.<Integer>withCapacity(5)
                .manyWriters()
                .oneReader()
                .blocking()
                .withGC()
                .build();
PrefilledOverwritingRingBuffer<Event> processorToConsumers =
        PrefilledRingBuffer.<Event>withCapacity(300 + 1)
                .fillWith(Event::new)
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
        Integer data = producersToProcessor.take();

        int key = processorToConsumers.nextKey();
        Event event = processorToConsumers.next(key);
        event.setData(data);
        processorToConsumers.put(key);
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

### Marshalling ring buffers

```java
MarshallingBlockingRingBuffer ringBuffer =
        MarshallingRingBuffer.withCapacity(Number.getNextPowerOfTwo(100))
                .oneWriter()
                .oneReader()
                .blocking(FailBusyWaitStrategy.writingTooSlow())
                .unsafe()
                .build();

int offset = ringBuffer.next(INT + CHAR);
ringBuffer.writeInt(offset, 55);
ringBuffer.writeChar(offset + INT, 'x');
ringBuffer.put(offset + INT + CHAR);

int offset = ringBuffer.take(INT + CHAR);
System.out.println(ringBuffer.readInt(offset));
System.out.println(ringBuffer.readChar(offset + INT));
ringBuffer.advance(offset + INT + CHAR);

DirectMarshallingRingBuffer ringBuffer =
        DirectMarshallingRingBuffer.withCapacity(2048)
                .manyWriters()
                .manyReaders()
                .copyClass()
                .build();

long offset = ringBuffer.next();
ringBuffer.writeBoolean(offset, true); offset += BOOLEAN;
ringBuffer.writeDouble(offset, 5D); offset += DOUBLE;
ringBuffer.put(offset);

long offset = ringBuffer.take(BOOLEAN + DOUBLE);
System.out.println(ringBuffer.readBoolean(offset)); offset += BOOLEAN;
System.out.println(ringBuffer.readDouble(offset));
ringBuffer.advance();
```