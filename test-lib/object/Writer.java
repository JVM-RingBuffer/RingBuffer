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

import org.ringbuffer.object.EmptyRingBuffer;
import org.ringbuffer.object.RingBuffer;
import test.Profiler;
import test.TestThreadGroup;

class Writer extends TestThread {
    static TestThreadGroup startGroupAsync(RingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(RingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static Writer startAsync(int numIterations, RingBuffer<Event> ringBuffer, Profiler profiler) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, RingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private Writer(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        EmptyRingBuffer<Event> ringBuffer = getEmptyRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            ringBuffer.put(new Event(numIterations));
        }
    }
}
