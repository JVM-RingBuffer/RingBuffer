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

import org.ringbuffer.marshalling.MarshallingRingBuffer;
import test.Profiler;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.Offsets.*;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(MarshallingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(MarshallingRingBuffer ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static Writer startAsync(int numIterations, MarshallingRingBuffer ringBuffer, Profiler profiler) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, MarshallingRingBuffer ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private Writer(int numIterations, MarshallingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        MarshallingRingBuffer ringBuffer = getMarshallingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.next();
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
