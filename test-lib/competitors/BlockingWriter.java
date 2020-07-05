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

package test.competitors;

import test.Profiler;
import test.TestThreadGroup;
import test.object.Event;

import java.util.concurrent.BlockingQueue;

class BlockingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(BlockingQueue<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BlockingWriter(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(BlockingQueue<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    private BlockingWriter(int numIterations, BlockingQueue<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        try {
            BlockingQueue<Event> queue = getBlockingQueue();
            for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
                queue.put(new Event(numIterations));
            }
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}
