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

package test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractRingBufferTest extends Benchmark {
    protected static final int NUM_ITERATIONS = 1_000_000;
    static final int CONCURRENCY = 3;
    protected static final int TOTAL_ELEMENTS = NUM_ITERATIONS * CONCURRENCY;

    protected static final long ONE_TO_ONE_SUM = getOneToOneSum();
    protected static final long ONE_TO_MANY_SUM = getOneToManySum();
    protected static final long MANY_WRITERS_SUM = ONE_TO_ONE_SUM * CONCURRENCY;

    private static long getOneToOneSum() {
        long result = 0L;
        for (int i = 1; i <= NUM_ITERATIONS; i++) {
            result += i;
        }
        return result;
    }

    private static long getOneToManySum() {
        long result = 0L;
        for (int i = 1; i <= TOTAL_ELEMENTS; i++) {
            result += i;
        }
        return result;
    }

    protected abstract long getSum();

    protected abstract long testSum();

    protected static Profiler createLatencyProfiler(int divideBy) {
        return new Profiler("Latency", divideBy);
    }

    @Override
    protected final int getNumIterations() {
        return 0;
    }

    @Override
    protected final void test(int i) {
        AbstractTestThread.resetThreadSpreader();

        assertEquals(getSum(), testSum());
    }
}
