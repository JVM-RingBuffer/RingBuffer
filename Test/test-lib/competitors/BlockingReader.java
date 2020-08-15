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

class BlockingReader extends Reader {
    static long runGroupAsync(BlockingQueue<Event> queue, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BlockingReader(numIterations, queue));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, BlockingQueue<Event> queue, Profiler profiler) {
        BlockingReader reader = new BlockingReader(numIterations, queue);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private BlockingReader(int numIterations, BlockingQueue<Event> queue) {
        super(numIterations, queue);
    }

    @Override
    long collect() {
        try {
            BlockingQueue<Event> queue = getBlockingQueue();
            long sum = 0L;
            for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
                sum += queue.take().getData();
            }
            return sum;
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}
