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

import org.ringbuffer.object.ConcurrentStack;
import test.Profiler;

class ConcurrentStackContentionTest extends RingBufferTest {
    private static class Holder {
        private static final ConcurrentStack<Event> stack = new ConcurrentStack<>(NOT_ONE_TO_ONE_SIZE * 2);

        static {
            for (int i = 0; i < stack.getCapacity() / 2; i++) {
                stack.push(new Event(0));
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentStackContentionTest test = new ConcurrentStackContentionTest();
        test.doNotCheckSum();
        test.runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.stack, profiler);
        return Reader.runGroupAsync(Holder.stack, profiler);
    }
}
