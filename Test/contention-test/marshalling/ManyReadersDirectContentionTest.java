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
import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyReadersDirectContentionTest extends RingBufferTest {
    public static final DirectClearingRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new ManyReadersDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        DirectClearingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedDirectClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
