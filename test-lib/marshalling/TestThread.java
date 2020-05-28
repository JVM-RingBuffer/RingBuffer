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

package test.marshalling;

import org.ringbuffer.AbstractRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import org.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.MarshallingRingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, AbstractRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    MarshallingRingBuffer getMarshallingRingBuffer() {
        return (MarshallingRingBuffer) ringBuffer;
    }

    MarshallingBlockingRingBuffer getMarshallingBlockingRingBuffer() {
        return (MarshallingBlockingRingBuffer) ringBuffer;
    }

    DirectMarshallingRingBuffer getDirectMarshallingRingBuffer() {
        return (DirectMarshallingRingBuffer) ringBuffer;
    }

    DirectMarshallingBlockingRingBuffer getDirectMarshallingBlockingRingBuffer() {
        return (DirectMarshallingBlockingRingBuffer) ringBuffer;
    }
}
