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

import org.ringbuffer.object.PrefilledOverwritingRingBuffer;
import test.Profiler;
import test.TestThreadGroup;

class PrefilledOverwritingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledOverwritingWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static PrefilledOverwritingWriter startAsync(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        PrefilledOverwritingWriter writer = new PrefilledOverwritingWriter(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private PrefilledOverwritingWriter(int numIterations, PrefilledOverwritingRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledOverwritingRingBuffer<Event> ringBuffer = getPrefilledOverwritingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            ringBuffer.next(key).setData(numIterations);
            ringBuffer.put(key);
        }
    }
}
