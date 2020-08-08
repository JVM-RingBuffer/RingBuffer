# RingBuffer

This library supplies FIFO ring buffer implementations that are optimized for different use cases and can be configured extensively. The following cases are covered:

- Single-Producer Single-Consumer
- Single-Producer Multiple-Consumer
- Multiple-Producer Single-Consumer
- Multiple-Producer Multiple-Consumer

Only busy-waiting is supported, and the way in which it is done can be configured, so even an exception may be thrown.
If ultra-low latency is not a requirement, there are ways to busy-wait without causing excessive CPU usage.

**Object ring buffers** work with Java objects.

They can be pre-filled, to support garbage-free operation.  
They support reading elements in batches, which improves throughput at the cost of reduced granularity.  
When full, they can either clear all elements, discard incoming elements, or they can block waiting for an element to be read.

**Marshalling ring buffers** are backed by a byte array and allow to transfer any primitive type.

They produce no garbage, and their capacity must be a power of 2 (`Numbers.getNextPowerOfTwo()` can help).  
The byte array can reside on or off the heap. In the latter case, more than ~2GB can be allocated.  
When full, they can either clear all contents or block waiting for enough space to become available.

## Thread priority and affinity

First, load the native library for the current platform: `Threads.loadNativeLibrary()`
- Bind threads to specific CPU cores: `Threads.bindCurrentThreadToCPU(int)`
- Set threads priority to realtime: `Threads.setCurrentThreadPriorityToRealtime()`

## Performance

Benchmarks were run on i7 8700.  
Latency is the time it takes for a single `Object { int }` or `int` to be written or read by latency-sensitive threads.

scenario|msg/sec|latency
---|---|---
3 producers → 3 consumers | 37 million | 77ns
3 producers → 1 consumer | 19 million | 52ns
1 producer → 3 consumers | 54 million | 2ns
1 producer → 1 consumer | 587 million | 1.5ns

The following are lock-free implementations (call `withoutLocks()` on the builder).  
They must never become full.

scenario|msg/sec|latency
---|---|---
3 producers → 3 consumers | 37 million | 81ns
3 producers → 1 consumer | 39 million | 25ns
1 producer → 3 consumers | 50 million | 1.5ns
1 producer → 1 consumer | 555 million | 1.5ns
2 producers → 1 processor → 2 consumers | 33 million | 27ns

The following are competitors: [Agrona](https://github.com/real-logic/agrona) and [JCTools](https://github.com/JCTools/JCTools), respectively.

scenario|msg/sec|latency
---|---|---
3 producers → 3 consumers | 13 million | 210ns
3 producers → 1 consumer | 15 million | 67ns
1 producer → 1 consumer | 200 million | 5ns

scenario|msg/sec|latency
---|---|---
3 producers → 3 consumers | 13 million | 220ns
3 producers → 1 consumer | 17 million | 57ns
1 producer → 3 consumers | 16 million | 5ns
1 producer → 1 consumer | 200 million | 5ns

## Class copying

To allow inlining of polymorphic calls, a class can be copied. This is similar to what happens with C++ templates.  
To understand when this can be useful, examine the particular implementation you are using and determine if a virtual call is polymorphic (for example this is the case when different busy-wait strategies are used in different instances of the same ring buffer).

First, add [Byte Buddy](https://bytebuddy.net/#/) to the classpath.  
Then, call `copyClass()` where available or use `CopiedClass` directly.

## Public utilities

To build a Java library for ultra-low-latency inter-thread communication, we introduced utilities.

- `Unsafe.UNSAFE` exposes the `jdk.internal.misc.Unsafe` without causing `IllegalAccessError`s.
- `Atomic*` classes wrap `Unsafe` calls, exposing all the features supported by `VarHandle`s while having better names and reducing indirection.
- `Platform.current()` returns the current OS and JVM architecture.
- `CleanerService` supports freeing off-heap memory on object GC.
- `GarbageCollectorProfiler` supports listening to GC events and logging them.
- `Assert`, `Assume` and `Ensure` perform the bare-minimum work to check conditions, and have better names.
- `*ArrayView`s allow to view an array as `List`.

## Download

Find artifacts and Maven coordinates in _Releases_.  
Build tools require [@Menzani's repository](https://www.menzani.eu/cdn/maven).

The module name is `org.ringbuffer`.  
`-XX:-RestrictContended` is recommended.

**Warning.**
These features of object ring buffers have not yet been tested:
- Discarding elements when full
- `forEach()`, `contains()` and `toString()`

You can [run the benchmarks and build from source](BUILD.md).

## Examples

### Object ring buffers

```java
RingBuffer<Integer> producersToProcessor =
        RingBuffer.<Integer>withCapacity(5)
                .manyWriters()
                .oneReader()
                .blocking()
                .withGC()
                .build();
PrefilledRingBuffer<Event> processorToConsumers =
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
MarshallingRingBuffer ringBuffer =
        MarshallingRingBuffer.withCapacity(Numbers.getNextPowerOfTwo(100))
                .oneWriter()
                .oneReader()
                .blocking(FailBusyWaitStrategy.readingTooSlow())
                .withByteArray(ByteArray.UNSAFE)
                .build();

int offset = ringBuffer.next(INT + CHAR);
ringBuffer.writeInt(offset, 55);
ringBuffer.writeChar(offset + INT, 'x');
ringBuffer.put(offset + INT + CHAR);

int offset = ringBuffer.take(INT + CHAR);
System.out.println(ringBuffer.readInt(offset));
System.out.println(ringBuffer.readChar(offset + INT));
ringBuffer.advance(offset + INT + CHAR);

DirectMarshallingClearingRingBuffer ringBuffer =
        DirectMarshallingRingBuffer.withCapacity(2048L)
                .manyWriters()
                .manyReaders()
                .copyClass()
                .withMemoryOrder(MemoryOrder.VOLATILE)
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