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

import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.Profiler;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.*;

class DirectWriter extends TestThread {
    static TestThreadGroup startGroupAsync(DirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(DirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static DirectWriter startAsync(int numIterations, DirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        DirectWriter writer = new DirectWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, DirectMarshallingRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private DirectWriter(int numIterations, DirectMarshallingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectMarshallingRingBuffer ringBuffer = getDirectMarshallingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next();
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
