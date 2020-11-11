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

import org.ringbuffer.object.PrefilledRingBuffer2;
import test.Profiler;
import test.TestThreadGroup;

class PrefilledWriter2 extends TestThread {
    private static TestThreadGroup startGroupAsync(PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new PrefilledWriter2(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static PrefilledWriter2 startAsync(int numIterations, PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        PrefilledWriter2 writer = new PrefilledWriter2(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, PrefilledRingBuffer2<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private PrefilledWriter2(int numIterations, PrefilledRingBuffer2<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        PrefilledRingBuffer2<Event> ringBuffer = getPrefilledRingBuffer2();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int key = ringBuffer.nextKey();
            int putKey = ringBuffer.nextPutKey(key);
            ringBuffer.next(key, putKey).setData(numIterations);
            ringBuffer.put(putKey);
        }
    }
}
