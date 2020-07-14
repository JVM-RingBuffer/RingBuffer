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

package org.ringbuffer.java;

public class IntPair {
    public static long of(int first, int second) {
        return (long) second << 32 | first & 0xFFFFFFFFL;
    }

    public static int getFirst(long intPair) {
        return (int) intPair;
    }

    public static int getSecond(long intPair) {
        return (int) (intPair >> 32);
    }
}
