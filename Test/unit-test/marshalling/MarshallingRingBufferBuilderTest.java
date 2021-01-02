package org.ringbuffer.marshalling;

import org.junit.jupiter.api.Test;
import org.ringbuffer.RingBufferBuilderTest;
import test.marshalling.*;

class MarshallingRingBufferBuilderTest extends RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(ConcurrentHeapRingBuffer.class, ManyToManyHeapContentionTest.RING_BUFFER);
        expectClass(ConcurrentHeapBlockingRingBuffer.class, ManyToManyHeapBlockingContentionTest.Holder.RING_BUFFER, ManyToManyHeapBlockingContentionPerfTest.RING_BUFFER);
        expectClass(ConcurrentDirectRingBuffer.class, ManyToManyDirectContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectBlockingRingBuffer.class, ManyToManyDirectBlockingContentionTest.Holder.RING_BUFFER, ManyToManyDirectBlockingContentionPerfTest.RING_BUFFER);

        expectClass(AtomicReadHeapRingBuffer.class, ManyReadersHeapContentionTest.RING_BUFFER);
        expectClass(AtomicReadHeapBlockingRingBuffer.class, ManyReadersHeapBlockingContentionTest.Holder.RING_BUFFER, ManyReadersHeapBlockingContentionPerfTest.RING_BUFFER);
        expectClass(AtomicReadDirectRingBuffer.class, ManyReadersDirectContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectBlockingRingBuffer.class, ManyReadersDirectBlockingContentionTest.Holder.RING_BUFFER, ManyReadersDirectBlockingContentionPerfTest.RING_BUFFER);

        expectClass(AtomicWriteHeapRingBuffer.class, ManyWritersHeapContentionTest.RING_BUFFER);
        expectClass(AtomicWriteHeapBlockingRingBuffer.class, ManyWritersHeapBlockingContentionTest.Holder.RING_BUFFER, ManyWritersHeapBlockingContentionPerfTest.RING_BUFFER);
        expectClass(AtomicWriteDirectRingBuffer.class, ManyWritersDirectContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectBlockingRingBuffer.class, ManyWritersDirectBlockingContentionTest.Holder.RING_BUFFER, ManyWritersDirectBlockingContentionPerfTest.RING_BUFFER);

        expectClass(VolatileHeapRingBuffer.class, OneToOneHeapContentionTest.RING_BUFFER);
        expectClass(VolatileHeapBlockingRingBuffer.class, OneToOneHeapBlockingContentionTest.Holder.RING_BUFFER, OneToOneHeapBlockingContentionPerfTest.RING_BUFFER);
        expectClass(VolatileDirectRingBuffer.class, OneToOneDirectContentionTest.RING_BUFFER);
        expectClass(VolatileDirectBlockingRingBuffer.class, OneToOneDirectBlockingContentionTest.Holder.RING_BUFFER, OneToOneDirectBlockingContentionPerfTest.RING_BUFFER);

        expectClass(LockfreeConcurrentHeapRingBuffer.class, LockfreeManyToManyHeapContentionTest.RING_BUFFER);
        expectClass(LockfreeConcurrentDirectRingBuffer.class, LockfreeManyToManyDirectContentionTest.RING_BUFFER);
        expectClass(LockfreeAtomicReadHeapRingBuffer.class, LockfreeManyReadersHeapContentionTest.RING_BUFFER);
        expectClass(LockfreeAtomicReadDirectRingBuffer.class, LockfreeManyReadersDirectContentionTest.RING_BUFFER);
        expectClass(LockfreeAtomicWriteHeapRingBuffer.class, LockfreeManyWritersHeapContentionTest.RING_BUFFER);
        expectClass(LockfreeAtomicWriteDirectRingBuffer.class, LockfreeManyWritersDirectContentionTest.RING_BUFFER);
        expectClass(LockfreeVolatileHeapRingBuffer.class, LockfreeOneToOneHeapContentionTest.RING_BUFFER);
        expectClass(LockfreeVolatileDirectRingBuffer.class, LockfreeOneToOneDirectContentionTest.RING_BUFFER);
    }
}
