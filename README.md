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

### Native ring buffers

```java
HeapBlockingRingBuffer heapRingBuffer =
        HeapRingBuffer.withCapacity(Number.getNextPowerOfTwo(100))
                .oneWriter()
                .oneReader()
                .blocking(FailBusyWaitStrategy.writingTooSlow())
                .unsafe()
                .build();

int offset = heapRingBuffer.next(INT + CHAR);
heapRingBuffer.writeInt(offset, 55);
heapRingBuffer.writeChar(offset + INT, 'x');
heapRingBuffer.put(offset + INT + CHAR);

int offset = heapRingBuffer.take(INT + CHAR);
System.out.println(heapRingBuffer.readInt(offset));
System.out.println(heapRingBuffer.readChar(offset + INT));
heapRingBuffer.advance(offset + INT + CHAR);

NativeRingBuffer nativeRingBuffer =
        NativeRingBuffer.withCapacity(2048)
                .manyWriters()
                .manyReaders()
                .copyClass()
                .build();

long offset = nativeRingBuffer.next();
nativeRingBuffer.writeBoolean(offset, true); offset += BOOLEAN;
nativeRingBuffer.writeDouble(offset, 5D); offset += DOUBLE;
nativeRingBuffer.put(offset);

long offset = nativeRingBuffer.take(BOOLEAN + DOUBLE);
System.out.println(nativeRingBuffer.readBoolean(offset)); offset += BOOLEAN;
System.out.println(nativeRingBuffer.readDouble(offset));
nativeRingBuffer.advance();
```