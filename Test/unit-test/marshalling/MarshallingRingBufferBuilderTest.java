package org.ringbuffer.marshalling;

import bench.marshalling.*;
import org.ringbuffer.RingBufferBuilderTest;

public class MarshallingRingBufferBuilderTest extends RingBufferBuilderTest {
    public void testClasses() {
        expectClass(ConcurrentHeapRingBuffer.class, ManyToManyHeapContentionBenchmark.RING_BUFFER);
        expectClass(ConcurrentHeapBlockingRingBuffer.class, ManyToManyHeapBlockingContentionBenchmark.Holder.RING_BUFFER, ManyToManyHeapBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(ConcurrentDirectRingBuffer.class, ManyToManyDirectContentionBenchmark.RING_BUFFER);
        expectClass(ConcurrentDirectBlockingRingBuffer.class, ManyToManyDirectBlockingContentionBenchmark.Holder.RING_BUFFER, ManyToManyDirectBlockingContentionPerfBenchmark.RING_BUFFER);

        expectClass(AtomicReadHeapRingBuffer.class, ManyReadersHeapContentionBenchmark.RING_BUFFER);
        expectClass(AtomicReadHeapBlockingRingBuffer.class, ManyReadersHeapBlockingContentionBenchmark.Holder.RING_BUFFER, ManyReadersHeapBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(AtomicReadDirectRingBuffer.class, ManyReadersDirectContentionBenchmark.RING_BUFFER);
        expectClass(AtomicReadDirectBlockingRingBuffer.class, ManyReadersDirectBlockingContentionBenchmark.Holder.RING_BUFFER, ManyReadersDirectBlockingContentionPerfBenchmark.RING_BUFFER);

        expectClass(AtomicWriteHeapRingBuffer.class, ManyWritersHeapContentionBenchmark.RING_BUFFER);
        expectClass(AtomicWriteHeapBlockingRingBuffer.class, ManyWritersHeapBlockingContentionBenchmark.Holder.RING_BUFFER, ManyWritersHeapBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(AtomicWriteDirectRingBuffer.class, ManyWritersDirectContentionBenchmark.RING_BUFFER);
        expectClass(AtomicWriteDirectBlockingRingBuffer.class, ManyWritersDirectBlockingContentionBenchmark.Holder.RING_BUFFER, ManyWritersDirectBlockingContentionPerfBenchmark.RING_BUFFER);

        expectClass(VolatileHeapRingBuffer.class, OneToOneHeapContentionBenchmark.RING_BUFFER);
        expectClass(VolatileHeapBlockingRingBuffer.class, OneToOneHeapBlockingContentionBenchmark.Holder.RING_BUFFER, OneToOneHeapBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(VolatileDirectRingBuffer.class, OneToOneDirectContentionBenchmark.RING_BUFFER);
        expectClass(VolatileDirectBlockingRingBuffer.class, OneToOneDirectBlockingContentionBenchmark.Holder.RING_BUFFER, OneToOneDirectBlockingContentionPerfBenchmark.RING_BUFFER);

        expectClass(LockfreeConcurrentHeapRingBuffer.class, LockfreeManyToManyHeapContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeConcurrentDirectRingBuffer.class, LockfreeManyToManyDirectContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeAtomicReadHeapRingBuffer.class, LockfreeManyReadersHeapContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeAtomicReadDirectRingBuffer.class, LockfreeManyReadersDirectContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeAtomicWriteHeapRingBuffer.class, LockfreeManyWritersHeapContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeAtomicWriteDirectRingBuffer.class, LockfreeManyWritersDirectContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeVolatileHeapRingBuffer.class, LockfreeOneToOneHeapContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeVolatileDirectRingBuffer.class, LockfreeOneToOneDirectContentionBenchmark.RING_BUFFER);
    }
}
