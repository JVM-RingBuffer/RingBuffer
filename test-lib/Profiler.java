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

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Profiler {
    static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##");

    private final Benchmark.Result result;
    private final double divideBy;
    private long start;

    public Profiler(Object object, int divideBy) {
        this(object.getClass().getSimpleName(), divideBy);
    }

    public Profiler(String name, int divideBy) {
        this(name, divideBy, value -> {
            if (value < 2_000D) {
                return NUMBER_FORMAT.format(value) + "ns";
            }
            if (value < 2_000_000D) {
                return NUMBER_FORMAT.format(value / 1_000D) + "us";
            }
            return NUMBER_FORMAT.format(value / 1_000_000D) + "ms";
        });
    }

    public Profiler(String name, int divideBy, Benchmark.Formatter averageFormatter) {
        this.divideBy = divideBy;
        result = Benchmark.current().getResult(name, averageFormatter);
    }

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        final long end = System.nanoTime();
        result.update(Math.round((end - start) / divideBy));
    }
}
