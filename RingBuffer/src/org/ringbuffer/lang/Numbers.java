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

package org.ringbuffer.lang;

public class Numbers {
    public static int ceilDiv(int dividend, int divisor) {
        return (dividend - 1) / divisor + 1;
    }

    public static int getNextPowerOfTwo(int value) {
        int highestOneBit = Integer.highestOneBit(value);
        if (value == highestOneBit) {
            return value;
        }
        return highestOneBit << 1;
    }

    public static long getNextPowerOfTwo(long value) {
        long highestOneBit = Long.highestOneBit(value);
        if (value == highestOneBit) {
            return value;
        }
        return highestOneBit << 1;
    }

    public static boolean isPowerOfTwo(int value) {
        return (value & -value) == value;
    }

    public static boolean isPowerOfTwo(long value) {
        return (value & -value) == value;
    }
}
