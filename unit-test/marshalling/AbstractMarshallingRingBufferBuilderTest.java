/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.marshalling;

import org.junit.jupiter.api.Test;
import org.ringbuffer.AbstractRingBufferBuilderTest;
import test.marshalling.*;

class AbstractMarshallingRingBufferBuilderTest extends AbstractRingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(ConcurrentHeapMarshallingRingBuffer.class, MarshallingManyToManyContentionTest.RING_BUFFER);
        expectClass(ConcurrentHeapMarshallingBlockingRingBuffer.class, MarshallingManyToManyBlockingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingRingBuffer.class, MarshallingManyToManyDirectContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingBlockingRingBuffer.class, MarshallingManyToManyDirectBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicReadHeapMarshallingRingBuffer.class, MarshallingManyReadersContentionTest.RING_BUFFER);
        expectClass(AtomicReadHeapMarshallingBlockingRingBuffer.class, MarshallingManyReadersBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingRingBuffer.class, MarshallingManyReadersDirectContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingBlockingRingBuffer.class, MarshallingManyReadersDirectBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicWriteHeapMarshallingRingBuffer.class, MarshallingManyWritersContentionTest.RING_BUFFER);
        expectClass(AtomicWriteHeapMarshallingBlockingRingBuffer.class, MarshallingManyWritersBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingRingBuffer.class, MarshallingManyWritersDirectContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingBlockingRingBuffer.class, MarshallingManyWritersDirectBlockingContentionTest.RING_BUFFER);

        expectClass(VolatileHeapMarshallingRingBuffer.class, MarshallingOneToOneContentionTest.RING_BUFFER);
        expectClass(VolatileHeapMarshallingBlockingRingBuffer.class, MarshallingOneToOneBlockingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingRingBuffer.class, MarshallingOneToOneDirectContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingBlockingRingBuffer.class, MarshallingOneToOneDirectBlockingContentionTest.RING_BUFFER);

        expectClass(FastConcurrentHeapMarshallingRingBuffer.class, FastMarshallingManyToManyContentionTest.RING_BUFFER);
        expectClass(FastConcurrentDirectMarshallingRingBuffer.class, FastMarshallingManyToManyDirectContentionTest.RING_BUFFER);
        expectClass(FastAtomicReadHeapMarshallingRingBuffer.class, FastMarshallingManyReadersContentionTest.RING_BUFFER);
        expectClass(FastAtomicReadDirectMarshallingRingBuffer.class, FastMarshallingManyReadersDirectContentionTest.RING_BUFFER);
        expectClass(FastAtomicWriteHeapMarshallingRingBuffer.class, FastMarshallingManyWritersContentionTest.RING_BUFFER);
        expectClass(FastAtomicWriteDirectMarshallingRingBuffer.class, FastMarshallingManyWritersDirectContentionTest.RING_BUFFER);
        expectClass(FastVolatileHeapMarshallingRingBuffer.class, FastMarshallingOneToOneContentionTest.RING_BUFFER);
        expectClass(FastVolatileDirectMarshallingRingBuffer.class, FastMarshallingOneToOneDirectContentionTest.RING_BUFFER);
    }
}
