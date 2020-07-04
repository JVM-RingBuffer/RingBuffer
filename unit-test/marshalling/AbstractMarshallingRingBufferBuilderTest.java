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
        expectClass(ConcurrentHeapMarshallingRingBuffer.class, ManyToManyMarshallingContentionTest.RING_BUFFER);
        expectClass(ConcurrentHeapMarshallingBlockingRingBuffer.class, ManyToManyMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingRingBuffer.class, ManyToManyDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingBlockingRingBuffer.class, ManyToManyDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicReadHeapMarshallingRingBuffer.class, ManyReadersMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicReadHeapMarshallingBlockingRingBuffer.class, ManyReadersMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingRingBuffer.class, ManyReadersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingBlockingRingBuffer.class, ManyReadersDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicWriteHeapMarshallingRingBuffer.class, ManyWritersMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteHeapMarshallingBlockingRingBuffer.class, ManyWritersMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingRingBuffer.class, ManyWritersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingBlockingRingBuffer.class, ManyWritersDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(VolatileHeapMarshallingRingBuffer.class, OneToOneMarshallingContentionTest.RING_BUFFER);
        expectClass(VolatileHeapMarshallingBlockingRingBuffer.class, OneToOneMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingRingBuffer.class, OneToOneDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingBlockingRingBuffer.class, OneToOneDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(FastConcurrentHeapMarshallingRingBuffer.class, FastManyToManyMarshallingContentionTest.RING_BUFFER);
        expectClass(FastConcurrentDirectMarshallingRingBuffer.class, FastManyToManyDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(FastAtomicReadHeapMarshallingRingBuffer.class, FastManyReadersMarshallingContentionTest.RING_BUFFER);
        expectClass(FastAtomicReadDirectMarshallingRingBuffer.class, FastManyReadersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(FastAtomicWriteHeapMarshallingRingBuffer.class, FastManyWritersMarshallingContentionTest.RING_BUFFER);
        expectClass(FastAtomicWriteDirectMarshallingRingBuffer.class, FastManyWritersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(FastVolatileHeapMarshallingRingBuffer.class, FastOneToOneMarshallingContentionTest.RING_BUFFER);
        expectClass(FastVolatileDirectMarshallingRingBuffer.class, FastOneToOneDirectMarshallingContentionTest.RING_BUFFER);
    }
}
