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

package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferManyReadersTest extends LinkedTransferManyReadersContentionTest {
    public static void main(String[] args) {
        new LinkedTransferManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
