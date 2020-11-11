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

package org.ringbuffer.util;

import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.lang.Lang;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentKeyedCounter<K> implements KeyedCounter<K> {
    private final ConcurrentMap<K, Count> counts = new ConcurrentHashMap<>();

    @Override
    public int increment(K key) {
        return counts.computeIfAbsent(key, k -> new Count()).getAndIncrement();
    }

    @Override
    public void reset(K key) {
        counts.remove(key);
    }

    @Override
    public void reset() {
        counts.clear();
    }

    private static class Count {
        private static final long VALUE = Lang.objectFieldOffset(Count.class, "value");

        private int value;

        private Count() {
            AtomicInt.setOpaque(this, VALUE, 1);
        }

        private int getAndIncrement() {
            return AtomicInt.getAndIncrementVolatile(this, VALUE);
        }
    }
}
