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

package test.object;

import org.ringbuffer.object.RingBuffer;
import org.ringbuffer.object.PrefilledClearingRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.ObjectRingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, Object ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @SuppressWarnings("unchecked")
    ObjectRingBuffer<Event> getRingBuffer() {
        return (ObjectRingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    RingBuffer<Event> getEmptyRingBuffer() {
        return (RingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    PrefilledClearingRingBuffer<Event> getPrefilledOverwritingRingBuffer() {
        return (PrefilledClearingRingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) ringBuffer;
    }
}
