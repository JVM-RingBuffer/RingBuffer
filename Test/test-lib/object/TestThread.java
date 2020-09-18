/*
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

import org.ringbuffer.object.ObjectRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;
import org.ringbuffer.object.RingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, ObjectRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @SuppressWarnings("unchecked")
    ObjectRingBuffer<Event> getObjectRingBuffer() {
        return (ObjectRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    RingBuffer<Event> getRingBuffer() {
        return (RingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer2<Event> getPrefilledRingBuffer2() {
        return (PrefilledRingBuffer2<Event>) dataStructure;
    }
}
