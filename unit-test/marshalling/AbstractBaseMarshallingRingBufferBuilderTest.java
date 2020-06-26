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

class AbstractBaseMarshallingRingBufferBuilderTest extends AbstractRingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(VolatileMarshallingRingBuffer.class, OneToOneMarshallingContentionTest.RING_BUFFER);
        expectClass(VolatileMarshallingBlockingRingBuffer.class, OneToOneMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingRingBuffer.class, OneToOneDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingBlockingRingBuffer.class, OneToOneDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicReadMarshallingRingBuffer.class, ManyReadersMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicReadMarshallingBlockingRingBuffer.class, ManyReadersMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingRingBuffer.class, ManyReadersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingBlockingRingBuffer.class, ManyReadersDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicWriteMarshallingRingBuffer.class, ManyWritersMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteMarshallingBlockingRingBuffer.class, ManyWritersMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingRingBuffer.class, ManyWritersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingBlockingRingBuffer.class, ManyWritersDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(ConcurrentMarshallingRingBuffer.class, ManyToManyMarshallingContentionTest.RING_BUFFER);
        expectClass(ConcurrentMarshallingBlockingRingBuffer.class, ManyToManyMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingRingBuffer.class, ManyToManyDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingBlockingRingBuffer.class, ManyToManyDirectMarshallingBlockingContentionTest.RING_BUFFER);
    }
}
