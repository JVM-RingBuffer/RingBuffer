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

package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import test.TestThreadGroup;

import static org.ringbuffer.marshalling.DirectOffsets.INT;

class DirectClearingReader extends HeapReader {
    static long runGroupAsync(DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectClearingReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        DirectClearingReader reader = new DirectClearingReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private DirectClearingReader(int numIterations, DirectClearingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        DirectClearingRingBuffer ringBuffer = getDirectClearingRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
        }
        return sum;
    }
}
