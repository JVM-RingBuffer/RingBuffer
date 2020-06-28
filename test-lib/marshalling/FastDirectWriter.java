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

import org.ringbuffer.marshalling.FastDirectMarshallingRingBuffer;
import test.Profiler;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.*;

class FastDirectWriter extends TestThread {
    static TestThreadGroup startGroupAsync(FastDirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new FastDirectWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(FastDirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static FastDirectWriter startAsync(int numIterations, FastDirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        FastDirectWriter writer = new FastDirectWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, FastDirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private FastDirectWriter(int numIterations, FastDirectMarshallingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        FastDirectMarshallingRingBuffer ringBuffer = getFastDirectMarshallingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset);
        }
    }
}
